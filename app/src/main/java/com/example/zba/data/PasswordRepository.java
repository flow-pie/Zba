package com.example.zba.data;

import android.os.Build;
import android.util.Log;

import com.example.zba.models.PasswordItem;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PasswordRepository {
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private static final String TAG = "PasswordRepository";
    public static final List <PasswordItem> passwordList = new ArrayList<>();

    //realtime listener
    public interface  PasswordListener {
        void onPasswordLoaded(List<PasswordItem> items);
    }

    public void getPasswords(PasswordListener listener) {
        db.collection("users")
                .document(uid)
                .collection("passwords")
                .addSnapshotListener((snapshot, error) -> {
                    if (error != null) {
                        Log.e(TAG, "Snapshot error", error);
                        return;
                    }

                List<PasswordItem> list = new ArrayList<>();
                if(snapshot != null) {
                    for (DocumentSnapshot doc : snapshot.getDocuments()){
                        PasswordItem item = doc.toObject(PasswordItem.class);
                        list.add(item);
                    }
                }
                listener.onPasswordLoaded(list);
                });
    }
    public static void addPassword(PasswordItem item) {
        String id = db.collection("users")
                .document(uid)
                .collection("passwords")
                .document()
                .getId();

        item.setId(id);

        db.collection("users")
                .document(uid)
                .collection("passwords")
                .document(id)
                .set(item)
                .addOnSuccessListener(v -> Log.d(TAG, "Password saved"))
                .addOnFailureListener(e -> Log.e(TAG, "Save failed", e));
    }

    public static void removePassword(String id) {
        db.collection("users")
                .document(uid)
                .collection("passwords")
                .document(id)
                .delete()
                .addOnSuccessListener(v -> Log.d(TAG, "Deleted"))
                .addOnFailureListener(e -> Log.e(TAG, "Delete failed", e));
    }
    public static void updatePassword(String id, String newAppName, String newUsername, String newPassword) {
        db.collection("users")
                .document(uid)
                .collection("passwords")
                .document(id)
                .update(
                        "appName", newAppName,
                        "userName", newUsername,
                        "password", newPassword
                )
                .addOnSuccessListener(v -> Log.d(TAG, "Updated"))
                .addOnFailureListener(e -> Log.e(TAG, "Update failed", e));
    }

    public static void clear(){
        passwordList.clear();
    }
}
