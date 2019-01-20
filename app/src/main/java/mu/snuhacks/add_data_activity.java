package mu.snuhacks;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

import mu.snuhacks.Adapters.NewsletterAdapter;

public class add_data_activity extends AppCompatActivity implements NewsletterAdapter.NewsletterAdapterInterface {
    private final String TAG = add_data_activity.class.getSimpleName();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference newsletterReference;
    private ChildEventListener childEventListener;
    private SharedPreferences preferences;

    private RecyclerView newsletterModView;
    private Button addButton;
    private Button addSuperuserButton;

    private NewsletterAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_data_activity_layout);
        firebaseDatabase = FirebaseDatabase.getInstance();
        newsletterReference = firebaseDatabase.getReference("/data/newsletter/");
        newsletterModView = (RecyclerView) findViewById(R.id.newsletter_mod_view);
        addButton = (Button) findViewById(R.id.add_button);
        addSuperuserButton = (Button) findViewById(R.id.add_superuser_button);
        preferences = getApplicationContext().getSharedPreferences("MyPref", 0);
        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        newsletterModView.setLayoutManager(manager);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_data_activity.this);
                LinearLayout linearLayout = new LinearLayout(add_data_activity.this);
                final EditText headingEditText = new EditText(add_data_activity.this);
                final EditText contentEditText = new EditText(add_data_activity.this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.addView(headingEditText);
                linearLayout.addView(contentEditText);
                builder.setView(linearLayout);
                builder.setTitle("Add newsletter");
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        newsletterReference.push().setValue(new firstActivity.NewsletterData(headingEditText.getText().toString()
                                ,contentEditText.getText().toString())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    dialog.dismiss();;
                                }
                            }
                        });
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"Addition cancelled");
                        dialog.dismiss();
                    }
                });
                AlertDialog addNewsletterDialog = builder.create();
                addNewsletterDialog.show();
            }
        });
        addSuperuserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(add_data_activity.this);
                LinearLayout layout = new LinearLayout(add_data_activity.this);
                layout.setOrientation(LinearLayout.VERTICAL);
                final EditText passEditText = new EditText(add_data_activity.this);
                passEditText.setHint("Enter your password here");
                passEditText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                final EditText usernameEditText = new EditText(add_data_activity.this);
                usernameEditText.setHint("Enter superuser username here");
                layout.addView(usernameEditText);
                layout.addView(passEditText);
                builder.setView(layout);
                builder.setTitle("Add superuser");
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        if(passEditText.getText().length() > 0) {
                            if (preferences.getString("password", "").equals(passEditText.getText().toString())) {
                                if (usernameEditText.getText().length() > 0) {
                                    DatabaseReference superUserReference = firebaseDatabase.getReference("/config_data/superuser").push();
                                    superUserReference.setValue(usernameEditText.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                dialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Superuser added", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Enter valid password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog superUserDialog = builder.create();
                superUserDialog.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (childEventListener != null) {
            newsletterReference.removeEventListener(childEventListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (childEventListener == null) {
            childEventListener = getChildEventListener();
        }
        newsletterReference.addChildEventListener(childEventListener);
    }

    private ChildEventListener getChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (adapter == null) {
                    adapter = new NewsletterAdapter(add_data_activity.this, getApplicationContext());
                    newsletterModView.setAdapter(adapter);
                }
                firstActivity.NewsletterData data = dataSnapshot.getValue(firstActivity.NewsletterData.class);
                data.setKey(dataSnapshot.getKey());
                adapter.add(data);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                firstActivity.NewsletterData data = dataSnapshot.getValue(firstActivity.NewsletterData.class);
                data.setKey(dataSnapshot.getKey());
                adapter.changeData(data);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                firstActivity.NewsletterData data = dataSnapshot.getValue(firstActivity.NewsletterData.class);
                data.setKey(dataSnapshot.getKey());
                adapter.remove(data);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,databaseError.getMessage());
            }
        };
    }

    @Override
    public void onEdit(final firstActivity.NewsletterData newsletter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(add_data_activity.this);
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText headingEditText = new EditText(this);
        final EditText contentEditText = new EditText(this);
        headingEditText.setText(newsletter.getHeading());
        contentEditText.setText(newsletter.getContent());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(headingEditText);
        linearLayout.addView(contentEditText);
        builder.setView(linearLayout);
        builder.setTitle("Edit newsletter");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String heading = headingEditText.getText().toString();
                String content = contentEditText.getText().toString();
                if(!newsletter.getHeading().equals(heading) && !newsletter.getContent().equals(content)){
                    dialog.dismiss();
                } else{
                    newsletterReference.child(newsletter.getKey()).setValue(new firstActivity.NewsletterData(heading,content)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"Edit cancelled");
                dialog.dismiss();
            }
        });
        AlertDialog editNewsletterDialog = builder.create();
        editNewsletterDialog.show();
    }

    @Override
    public void onDelete(final firstActivity.NewsletterData newsletter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(add_data_activity.this);
        builder.setTitle("Confirm deletion");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(final DialogInterface dialog, int which) {
                newsletterReference.child(newsletter.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"Deletion cancelled");
                dialog.dismiss();
            }
        });
        AlertDialog deleteDialog = builder.create();
        deleteDialog.show();
    }

    @Override
    public void sendNotification(final firstActivity.NewsletterData newsletter) {
        AlertDialog.Builder builder = new AlertDialog.Builder(add_data_activity.this);
        builder.setTitle("Send Notification?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                OkHttpClient client = new OkHttpClient();
                JSONObject object = new JSONObject();
                try {
                    object.put("heading", newsletter.getHeading());
                    object.put("content", newsletter.getContent());
                    object.put("api_key", getString(R.string.firebase_cf_api_key));
                    Request request = new Request.Builder()
                            .url("https://us-central1-snuhacks.cloudfunctions.net/sendMessage")
                            .addHeader("Content-Type","application/x-www-form-urlencoded")
                            .post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"),object.toString()))
                            .build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d(TAG,"Failure");
                        }

                        @Override
                        public void onResponse(final Response response){
                            Handler handler = new Handler(getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(add_data_activity.this,response.toString(),Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });
                        }
                    });
                } catch(Exception exception){
                    Log.d(TAG,"Exception:- " + exception.getMessage());
                }
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG,"Send notification cancelled");
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
