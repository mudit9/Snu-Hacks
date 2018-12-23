package mu.snuhacks;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class add_data_activity extends AppCompatActivity {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference newsletterReference;
    private ChildEventListener childEventListener;
    EditText name_text ;
    EditText heading_text;
    EditText content_text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                newsletterReference.child(String.valueOf(name_text.getText())).setValue(new firstActivity.NewsletterData(String.valueOf(heading_text.getText()),String.valueOf(content_text.getText())));
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        final AlertDialog alert = builder.create();

        setContentView(R.layout.add_data_activity_layout);
        FrameLayout add_data_layout = findViewById(R.id.add_data_layout);
        name_text = add_data_layout.findViewById(R.id.name_text);
        heading_text = add_data_layout.findViewById(R.id.heading_text);
        content_text = add_data_layout.findViewById(R.id.content_text);
        Button add_data_button = add_data_layout.findViewById(R.id.add_data_button);

        firebaseDatabase = FirebaseDatabase.getInstance();
        newsletterReference = firebaseDatabase.getReference("/data/newsletter/");

        add_data_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
            }
        });
    }
}
