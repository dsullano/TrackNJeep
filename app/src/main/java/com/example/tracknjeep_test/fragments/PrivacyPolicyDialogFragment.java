package com.example.tracknjeep_test.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class PrivacyPolicyDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Privacy Policy")
                .setMessage("JeepNTrack operates the TrackNJeep mobile application. This page informs you of our policies regarding the collection, use, and disclosure of personal data when you use our App and the choices you have associated with that data.\n" +
                        "\n" +
                        "We use your data to provide and improve the App. By using the App, you agree to the collection and use of information in accordance with this policy.\n" +
                        "\n" +
                        "Information Collection and Use\n" +
                        "\n" +
                        "We collect several different types of information for various purposes to provide and improve our App to you.\n" +
                        "\n" +
                        "Types of Data Collected\n" +
                        "\n" +
                        "Location Data\n" +
                        "\n" +
                        "We may collect precise location data about your device when you use our App and have granted us permission to do so. We use this information to provide location-based services within the App, such as providing you accurate results for the recommended routes our app will give you.\n" +
                        "\n" +
                        "Usage Data\n" +
                        "\n" +
                        "We may also collect information on how the App is accessed and used. This Usage Data may include information such as your device's Internet Protocol address (e.g., IP address), device type, device version, operating system version, unique device identifiers, and other diagnostic data.\n" +
                        "\n" +
                        "Use of Data\n" +
                        "\n" +
                        "We do not sell, trade, or otherwise transfer your personal data to third parties for marketing or advertising purposes. We only use the information collected for the purpose of providing and improving the App and its features.\n" +
                        "\n" +
                        "Data Retention\n" +
                        "\n" +
                        "We will retain your personal data only for as long as is necessary for the purposes set out in this Privacy Policy. We will retain and use your personal data to the extent necessary to comply with our legal obligations (for example, if we are required to retain your data to comply with applicable laws), resolve disputes, and enforce our legal agreements and policies.\n" +
                        "\n" +
                        "Security of Data\n" +
                        "\n" +
                        "The security of your data is of utmost importance to us. We employ industry-standard security measures to protect your personal information from unauthorized access, alteration, disclosure, or destruction. However, please be aware that no method of transmission over the internet or electronic storage is 100% secure and reliable, and we cannot guarantee its absolute security.\n" +
                        "\n" +
                        "We implement a variety of security measures to maintain the safety of your personal information when you access our App. These measures include but are not limited to:\n" +
                        "\n" +
                        "Encryption: We use encryption techniques to protect your data during transmission between your device and our servers.\n" +
                        "\n" +
                        "Secure Storage: Your personal information is stored on secure servers with restricted access to authorized personnel only.\n" +
                        "\n" +
                        "Regular Security Audits: We conduct regular security audits and assessments of our systems to identify and address potential vulnerabilities.\n" +
                        "\n" +
                        "Despite our best efforts to protect your personal information, please be aware that no method of transmission over the internet or electronic storage is completely secure. Therefore, we cannot guarantee the absolute security of your data.\n" +
                        "\n" +
                        "Data Breach\n" +
                        "\n" +
                        "In the event of a data breach that compromises the security of your personal information, we will promptly notify you and relevant authorities in accordance with applicable laws and regulations.\n" +
                        "\n" +
                        "Third-Party Services\n" +
                        "\n" +
                        "We may use third-party services, such as cloud hosting providers and analytics services, to assist us in operating the App and analyzing how it is used. These third-party service providers have access to your personal information only to perform these tasks on our behalf and are obligated not to disclose or use it for any other purpose.\n" +
                        "\n" +
                        "Changes to This Privacy Policy\n" +
                        "\n" +
                        "We reserve the right to update or change our Privacy Policy at any time. Any changes we make to this Privacy Policy will be posted on this page with an updated revision date. We encourage you to review this Privacy Policy periodically for any changes. Your continued use of the App after we post any modifications to the Privacy Policy on this page will constitute your acknowledgment of the modifications and your consent to abide and be bound by the modified Privacy Policy.")
                .setPositiveButton("I Accept", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Close the dialog
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
