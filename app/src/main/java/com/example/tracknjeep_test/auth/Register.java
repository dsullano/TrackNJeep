package com.example.tracknjeep_test.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.example.tracknjeep_test.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class Register extends BottomSheetDialogFragment {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    private TextInputEditText editTextEmail, editTextPassword, editTextFirstName, editTextLastName, editTextGender, editTextBirthdate;
    private Button btnRegister;
    private ProgressBar progBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_register, container, false);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://tracknjeep-f4109-default-rtdb.asia-southeast1.firebasedatabase.app");

        editTextEmail = view.findViewById(R.id.emailText);
        editTextPassword = view.findViewById(R.id.passwordText);
        editTextFirstName = view.findViewById(R.id.firstNameText);
        editTextLastName = view.findViewById(R.id.lastNameText);
        editTextGender = view.findViewById(R.id.genderText);
        progBar = view.findViewById(R.id.registerProgBar);
        btnRegister = view.findViewById(R.id.registerBtn);
        editTextBirthdate = view.findViewById(R.id.birthdateText);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        initializeJeepneyRoutes(); // Initialize jeepney routes on app start

        return view;
    }

    private void registerUser() {
        progBar.setVisibility(View.VISIBLE);
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String lastName = editTextLastName.getText().toString().trim();
        String gender = editTextGender.getText().toString().trim();
        String birthdate = editTextBirthdate.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(firstName) ||
                TextUtils.isEmpty(lastName) || TextUtils.isEmpty(gender)) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            progBar.setVisibility(View.GONE);
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();

                            if (firebaseUser != null) {
                                ReadWriteUserDetails userDetails = new ReadWriteUserDetails(firstName, lastName, gender, email);
                                DatabaseReference usersRef = database.getReference("users");

                                usersRef.child(firebaseUser.getUid()).setValue(userDetails)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getActivity(), "Account Created Successfully", Toast.LENGTH_SHORT).show();
                                                    // Launch login dialog
                                                    Login login = new Login();
                                                    FragmentManager fragmentManager = getParentFragmentManager();
                                                    login.show(fragmentManager, Login.class.getSimpleName());
                                                    dismiss();
                                                } else {
                                                    Log.e("Register", "Error adding user data to Realtime Database", task.getException());
                                                    Toast.makeText(getActivity(), "Failed to create account.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void initializeJeepneyRoutes() {
        DatabaseReference jeepneyRef = database.getReference("tblJeepney");

        Map<String, Jeepney> jeepneyRoutes = new HashMap<>();
        jeepneyRoutes.put("01B", new Jeepney("01B", "URGELLO - COLON", "URGELLO - LEON KILAT - PLAZA INDEPENCIA - PIER 1 - PIER 2 - PIER 3 - PIER 4 - WHITE GOLD - MJ CUENCO - BONIFACIO - SANCIANGKO - URGELLO"));
        jeepneyRoutes.put("01I", new Jeepney("01I", "PRIVATE – COLON – PIER – SM – AYALA", "SM – AYALA – AYALA ACCESS ROAD – HIPPODROMO – MJ CUENCO – BONIFACIO – SANCIANGKO – LEON KILAT – P DEL ROSARIO EXTENSION (PRIVATE) – LEON KIL"));
        jeepneyRoutes.put("02B", new Jeepney("02B", "SOUTH BUS TERMINAL - COLON", "SOUTH BUS TERMINAL - COLON - SOUTHBUS TERMINAL - CCMC - PANGANIBAN - COLON - PLAZA INDEPENDENCIA - PIER 1 - PIER 2 - PIER 3 - PIER 4 - WHITE GOLD - MJ CUENCO - BONIFACIO"));
        jeepneyRoutes.put("03B", new Jeepney("03B", "MABOLO JONES CARBON", "PANAGDAIT – MABOLO – AYALA – GORORDO – MANGO – FUENTE – JONES – CARBON"));
        jeepneyRoutes.put("03Q", new Jeepney("03Q", "AYALA – SM", "AYALA – JUAN LUNA – SM – JUAN LUNA – AYALA"));
        jeepneyRoutes.put("04C", new Jeepney("04C", "Lahug-Ramos-Carbon #", "Lahug-Ramos-Carbon # Plaza Housing(Busay)-Gorordo-Gen Maxilom-F Ramos-Junquera-Sanciangko-Panganiban-M C Briones-Lapu"));
        jeepneyRoutes.put("04L", new Jeepney("04L", "LAHUG – AYALA – SM", "AYALA – JUAN LUNA – SM – JUAN LUNA – SALINAS – JY SQUARE – GORORDO – UNIVERSITY OF THE PHILIPPINES – ESCARIO – AYALA"));
        jeepneyRoutes.put("04M", new Jeepney("04M", "LAHUG – AYALA", "AYALA – ESCARIO – GORORDO – UNIVERSITY OF THE PHILIPPINES – JY SQUARE – SALINAS – WATERFRONT – *U TURN+ – SALINAS – JY SQUARE - USPF CEBU"));
        jeepneyRoutes.put("06C", new Jeepney("06C", "GUADALUPE – CARBON", "SM – AYALA – AYALA ACCESS ROAD – HIPPODROMO – MJ CUENCO – BONIFACIO – SANCIANGKO – LEON KILAT – P DEL ROSARIO EXTENSION (PRIVATE) – LEON KIL"));
        jeepneyRoutes.put("06D", new Jeepney("06D", "SINGSON-MAGALLANES", "GUADALUPE – M VELEZ – CAPITOL – FUENTE – JONES – PLARIDEL – CARBON – MJ CUENCO – JONES – FUENTE – CAPTOL – M VELEZ – GUADALUPE"));
        jeepneyRoutes.put("06H", new Jeepney("06H", "GUADALUPE – AYALA – SM", "GUADALUPE – ENGLISH – V RAMA – P RODRIGUEZ – FUENTE – MANGO – WHITE GOLD CLUB – SM – JUAN LUNA – AYALA – ESCARIO – CAPITOL – M VELEZ – GUADALUPE"));
        jeepneyRoutes.put("08G", new Jeepney("08G", "ALUMNOS – COLON – MANALILI", "ALUMNOS – C PADILLA – COLON – MANALILI – C PADILLA – ALUMNOS"));
        jeepneyRoutes.put("09F", new Jeepney("09F", "BASAK – PLARIDEL – JUNQUERA – MANALILI", "BASAK – BACALSO – SANTO ROSARIO – JUNQUERA – COLON – MANALILI – PLARIDEL – MAGALLANES"));
        jeepneyRoutes.put("10M", new Jeepney("10M", "BULACAO – PARDO – COLON – SM", "SM – WHITE GOLD CLUB – MJ CUENCO – LEGASPI – COLON – BORROMEO – PANGANIBAN – BACALSO – MAMBALING – BASAK – PARDO – BULACAO"));
        jeepneyRoutes.put("11D", new Jeepney("11D", "PRIVATE – COLON – PIER – SM – AYALA", "SM – AYALA – AYALA ACCESS ROAD – HIPPODROMO – MJ CUENCO – BONIFACIO – SANCIANGKO – LEON KILAT – P DEL ROSARIO EXTENSION (PRIVATE) – LEON KIL"));
        jeepneyRoutes.put("12G", new Jeepney("12G", "PRIVATE – COLON – PIER – SM – AYALA", "SM – AYALA – AYALA ACCESS ROAD – HIPPODROMO – MJ CUENCO – BONIFACIO – SANCIANGKO – LEON KILAT – P DEL ROSARIO EXTENSION (PRIVATE) – LEON KIL"));
        jeepneyRoutes.put("10F", new Jeepney("10F", "BULACAO-PARDO-COLON # BULACAO-SOUTH ROAD-N BACALSO-DEL ROSARIO-JUNQUERA-COLON-A BORROMEO-SANCIANGKO-PAN", "BULACAO - PARDO - BASAK"));
        jeepneyRoutes.put("10H", new Jeepney("10H", "INAYAWAN - PARDO - COLON", "INAYAWAN - BULACAO-PARDO-COLON # BULACAO-SOUTH ROAD-N BACALSO-DEL ROSARIO-JUNQUERA-COLON-A BORROMEO-SANCIANGKO-PAN"));
        jeepneyRoutes.put("40A", new Jeepney("40A", "TALAMBAN - JY SQUARE", "TALAMBAN - BANILAD - ASILO - GORORDO - JY SQUARE"));
        jeepneyRoutes.put("41B", new Jeepney("41B", "LAHUG - IT PARK", "LAHUG - GORORDO - IT PARK"));
        jeepneyRoutes.put("42C", new Jeepney("42C", "MABOLO - CARBON", "MABOLO - SM CITY - AYALA - COLON - CARBON"));
        jeepneyRoutes.put("43D", new Jeepney("43D", "BASAK - CEBU CITY HALL", "BASAK - TABUNOK - PARDO - COLON - CEBU CITY HALL"));
        jeepneyRoutes.put("44E", new Jeepney("44E", "MAMBALING - NORTH BUS TERMINAL", "MAMBALING - SOUTH BUS TERMINAL - COLON - NORTH BUS TERMINAL"));
        jeepneyRoutes.put("45F", new Jeepney("45F", "TISA - AYALA", "TISA - LABANGON - V. RAMA - AYALA"));
        jeepneyRoutes.put("46G", new Jeepney("46G", "GUADALUPE - IT PARK", "GUADALUPE - MANGO - IT PARK"));
        jeepneyRoutes.put("47H", new Jeepney("47H", "INAYAWAN - PARDO - SM CITY", "INAYAWAN - BULACAO - PARDO - COLON - SM CITY"));
        jeepneyRoutes.put("48I", new Jeepney("48I", "LAHUG - SM CITY", "LAHUG - GORORDO - JUAN LUNA - SM CITY"));
        jeepneyRoutes.put("49J", new Jeepney("49J", "MABOLO - AYALA", "MABOLO - SM CITY - AYALA"));
        jeepneyRoutes.put("50K", new Jeepney("50K", "TALAMBAN - COLON", "TALAMBAN - BANILAD - ASILO - GORORDO - FUENTE - COLON"));
        jeepneyRoutes.put("51L", new Jeepney("51L", "BULACAO - CEBU CITY HALL", "BULACAO - PARDO - BASAK - TABUNOK - CEBU CITY HALL"));
        jeepneyRoutes.put("52M", new Jeepney("52M", "MAMBALING - CARBON", "MAMBALING - SOUTH BUS TERMINAL - COLON - CARBON"));
        jeepneyRoutes.put("53N", new Jeepney("53N", "TISA - IT PARK", "TISA - LABANGON - V. RAMA - IT PARK"));
        jeepneyRoutes.put("54O", new Jeepney("54O", "GUADALUPE - AYALA", "GUADALUPE - MANGO - AYALA"));
        jeepneyRoutes.put("55P", new Jeepney("55P", "INAYAWAN - PARDO - AYALA", "INAYAWAN - BULACAO - PARDO - COLON - AYALA"));
        jeepneyRoutes.put("56Q", new Jeepney("56Q", "LAHUG - COLON", "LAHUG - GORORDO - GEN MAXILOM - FUENTE - JONES - COLON"));
        jeepneyRoutes.put("57R", new Jeepney("57R", "TALAMBAN - SM CITY", "TALAMBAN - BANILAD - SM CITY"));
        jeepneyRoutes.put("58S", new Jeepney("58S", "MABOLO - JY SQUARE", "MABOLO - SM CITY - AYALA - JY SQUARE"));
        jeepneyRoutes.put("59T", new Jeepney("59T", "BASAK - IT PARK", "BASAK - TABUNOK - PARDO - COLON - IT PARK"));
        jeepneyRoutes.put("60U", new Jeepney("60U", "MAMBALING - COLON", "MAMBALING - SOUTH BUS TERMINAL - COLON"));
        jeepneyRoutes.put("61V", new Jeepney("61V", "TISA - SM CITY", "TISA - LABANGON - V. RAMA - SM CITY"));
        jeepneyRoutes.put("62W", new Jeepney("62W", "GUADALUPE - COLON", "GUADALUPE - MANGO - COLON"));
        jeepneyRoutes.put("63X", new Jeepney("63X", "INAYAWAN - IT PARK", "INAYAWAN - BULACAO - PARDO - COLON - IT PARK"));
        jeepneyRoutes.put("64Y", new Jeepney("64Y", "LAHUG - CARBON", "LAHUG - GORORDO - GEN MAXILOM - FUENTE - JONES - COLON - CARBON"));
        jeepneyRoutes.put("65Z", new Jeepney("65Z", "TALAMBAN - AYALA", "TALAMBAN - BANILAD - ASILO - GORORDO - AYALA"));
        jeepneyRoutes.put("12I", new Jeepney("12I", "LAHUG - COLON", "LAHUG - GORORDO - GEN MAXILOM - FUENTE - JONES - COLON"));
        jeepneyRoutes.put("13C", new Jeepney("13C", "TALAMBAN - COLON", "TALAMBAN - BANILAD - ASILO - GORORDO - FUENTE - COLON"));
        jeepneyRoutes.put("14D", new Jeepney("14D", "BULACAO - CEBU CITY HALL", "BULACAO - PARDO - BASAK - TABUNOK - CEBU CITY HALL"));
        jeepneyRoutes.put("15A", new Jeepney("15A", "MAMBALING - NORTH BUS TERMINAL", "MAMBALING - SOUTH BUS TERMINAL - COLON - NORTH BUS TERMINAL"));
        jeepneyRoutes.put("17B", new Jeepney("17B", "TALAMBAN - CEBU BUSINESS PARK", "TALAMBAN - BANILAD - IT PARK - CEBU BUSINESS PARK"));
        jeepneyRoutes.put("21A", new Jeepney("21A", "MANDAUE CITY - COLON", "MANDAUE CITY - MALLS - COLON"));
        jeepneyRoutes.put("22D", new Jeepney("22D", "MABOLO - COLON", "MABOLO - AYALA - IT PARK - COLON"));
        jeepneyRoutes.put("23C", new Jeepney("23C", "LABANGON - AYALA", "LABANGON - JONES - AYALA"));
        jeepneyRoutes.put("24D", new Jeepney("24D", "BASAK - COLON", "BASAK - PARDO - BULACAO - COLON"));
        jeepneyRoutes.put("25G", new Jeepney("25G", "TISA - CARBON", "TISA - LABANGON - COLON - CARBON"));
        jeepneyRoutes.put("26C", new Jeepney("26C", "LILOAN - MANDAUE - COLON", "LILOAN - MANDAUE - AS FORTUNA - COLON"));
        jeepneyRoutes.put("27B", new Jeepney("27B", "LAPU-LAPU CITY - MANDAUE CITY", "LAPU-LAPU CITY - MACTAN - MANDAUE CITY"));
        jeepneyRoutes.put("28D", new Jeepney("28D", "TISA - JY SQUARE", "TISA - LABANGON - V. RAMA - CAPITOL - JY SQUARE"));
        jeepneyRoutes.put("29A", new Jeepney("29A", "PIT-OS - TALAMBAN - COLON", "PIT-OS - TALAMBAN - BANILAD - ASILO - GORORDO - FUENTE - COLON"));
        jeepneyRoutes.put("30C", new Jeepney("30C", "MABOLO - PIER", "MABOLO - SM CITY - AYALA - PIER"));
        jeepneyRoutes.put("31G", new Jeepney("31G", "LAHUG - JY SQUARE - IT PARK", "LAHUG - JY SQUARE - IT PARK - CEBU BUSINESS PARK - AYALA"));
        jeepneyRoutes.put("32B", new Jeepney("32B", "PIT-OS - AYALA", "PIT-OS - TALAMBAN - BANILAD - AYALA"));
        jeepneyRoutes.put("33D", new Jeepney("33D", "CEBU CITY HALL - TALAMBAN", "CEBU CITY HALL - COLON - BANILAD - TALAMBAN"));
        jeepneyRoutes.put("34A", new Jeepney("34A", "TISA - PIER", "TISA - LABANGON - V. RAMA - PIER"));
        jeepneyRoutes.put("35C", new Jeepney("35C", "COLON - IT PARK", "COLON - FUENTE - MANGO - IT PARK"));
        jeepneyRoutes.put("36G", new Jeepney("36G", "TALAMBAN - SM CITY", "TALAMBAN - BANILAD - SM CITY"));
        jeepneyRoutes.put("37B", new Jeepney("37B", "MABOLO - CARBON", "MABOLO - SM CITY - COLON - CARBON"));
        jeepneyRoutes.put("38D", new Jeepney("38D", "PIT-OS - LAHUG", "PIT-OS - TALAMBAN - BANILAD - LAHUG"));
        jeepneyRoutes.put("39A", new Jeepney("39A", "TISA - AYALA", "TISA - LABANGON - V. RAMA - AYALA"));
        jeepneyRoutes.put("44A", new Jeepney("44A", "MAMBALING - AYALA CENTER CEBU", "MAMBALING - PUNTA - INAYAWAN - V RAMA - AYALA CENTER CEBU"));
        jeepneyRoutes.put("45B", new Jeepney("45B", "CARBON - TALAMBAN", "CARBON - COLON - FUENTE - BANILAD - TALAMBAN"));
        jeepneyRoutes.put("46C", new Jeepney("46C", "LAPU-LAPU CITY - SM CITY CEBU", "LAPU-LAPU CITY - PAJAC - MEPZ 1 - MEPZ 2 - IBO - OLD AIRPORT RD - UGONG - SM CITY CEBU"));
        jeepneyRoutes.put("47D", new Jeepney("47D", "CONSOLACION - COLON", "CONSOLACION - LILOAN - PAJAC - PUSOK - LAPU-LAPU CITY - MEPZ 1 - MEPZ 2 - IBO - OLD AIRPORT RD - UGONG - SM CITY CEBU - COLON"));
        jeepneyRoutes.put("48E", new Jeepney("48E", "MANDAUE CITY - TALAMBAN", "MANDAUE CITY - JAGOBIAO - PAGSABUNGAN - CASUNTINGAN - TALAMBAN"));
        jeepneyRoutes.put("49F", new Jeepney("49F", "NAGA - CEBU CITY HALL", "NAGA - TINAAN - SAN FERNANDO - CARCAR - TALISAY - SRP - CEBU CITY HALL"));
        jeepneyRoutes.put("50G", new Jeepney("50G", "CORDOVA - MAMBALING", "CORDOVA - PILAR - PAJAC - OLD AIRPORT RD - PAKNAAN - CONSOLACION - SM CITY CEBU - MAMBALING"));
        jeepneyRoutes.put("51H", new Jeepney("51H", "TALISAY - MANDAUE CITY", "TALISAY - BULACAO - SRP - CEBU CITY HALL - NORTH RECLAMATION AREA - MANDAUE CITY"));
        jeepneyRoutes.put("52I", new Jeepney("52I", "SAN FERNANDO - LAPU-LAPU CITY", "SAN FERNANDO - CARCAR - TALISAY - SRP - IL CORSO - LAWAAN - CORDOVA - BASAK - PAJAC - GY - MARIGONDON"));
        jeepneyRoutes.put("53J", new Jeepney("53J", "TOLEDO CITY - COLON", "TOLEDO CITY - PINAMUNGAHAN - NAGA - SAN FERNANDO - CARCAR - SRP - CEBU CITY HALL - COLON"));
        jeepneyRoutes.put("54K", new Jeepney("54K", "BARILI - MAMBALING", "BARILI - CARCAR - TALISAY - SRP - CEBU CITY HALL - SOUTH RECLAMATION AREA - IL CORSO - CORDOVA - IBO - OLD AIRPORT RD - UGONG - SM CITY CEBU - MAMBALING"));
        jeepneyRoutes.put("55L", new Jeepney("55L", "ARGAO - COLON", "ARGAO - DALAGUETE - ALCOY - BOLJOON - OSLOB - SANTANDER - TOLEDO CITY - PINAMUNGAHAN - NAGA - SAN FERNANDO - CARCAR - SRP - CEBU CITY HALL - COLON"));
        jeepneyRoutes.put("16K", new Jeepney("16K", "TALISAY - COLON", "TALISAY - SRP - CEBU CITY HALL - COLON"));
        jeepneyRoutes.put("17L", new Jeepney("17L", "CARCAR - MANDAUE CITY", "CARCAR - SAN FERNANDO - NAGA - TOLEDO CITY - PINAMUNGAHAN - CEBU CITY HALL - MANDAUE CITY"));
        jeepneyRoutes.put("18M", new Jeepney("18M", "NAGA - IT PARK", "NAGA - SAN FERNANDO - CARCAR - TALISAY - SRP - CEBU CITY HALL - FUENTE - IT PARK"));
        jeepneyRoutes.put("19N", new Jeepney("19N", "BOLJOON - SM CITY CEBU", "BOLJOON - OSLOB - SANTANDER - TOLEDO CITY - PINAMUNGAHAN - CEBU CITY HALL - SM CITY CEBU"));
        jeepneyRoutes.put("20O", new Jeepney("20O", "ALCOY - AYALA CENTER CEBU", "ALCOY - BOLJOON - OSLOB - SANTANDER - TOLEDO CITY - PINAMUNGAHAN - NAGA - SAN FERNANDO - CARCAR - TALISAY - SRP - CEBU CITY HALL - AYALA CENTER CEBU"));


        for (Map.Entry<String, Jeepney> entry : jeepneyRoutes.entrySet()) {
            String code = entry.getKey();
            Jeepney jeepney = entry.getValue();

            jeepneyRef.child(code).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (!snapshot.exists()) {
                        jeepneyRef.child(code).setValue(jeepney)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("Initialize", "Jeepney route added: " + code);
                                        } else {
                                            Log.e("Initialize", "Error adding jeepney route: " + code, task.getException());
                                        }
                                    }
                                });
                    } else {
                        Log.d("Initialize", "Jeepney route already exists: " + code);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("Initialize", "Database error: " + error.getMessage());
                }
            });
        }
    }
}
