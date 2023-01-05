package com.example.androidfinalproject.model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

//import com.example.androidfinalproject.MyApplication;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedMap;

public class ModelFirebase {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();
//    FirebaseStorage storage = FirebaseStorage.getInstance();

    public ModelFirebase() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        db.setFirestoreSettings(settings);
    }

    public interface GetReviewsListener {
        void onComplete(List<Review> reviewList);
    }

    public interface GetUserDetailsListener {
        void onComplete(User userDetails);
    }

    public interface DeleteReviewListener {
        void onComplete();
    }

    public interface Register {
        void onSuccess();

        void onFailed(String failReason);
    }

    public interface UpdatePassword {
        void onSuccess();

        void onFailed(String failReason);
    }

    public interface RegisterDetails {
        void onSuccess();
    }

    public interface SignIn {
        void onSuccess();

        void onFailed();
    }

    public interface SaveImageListener {
        void onComplete(String url);
    }

    public interface DeleteImageListener {
        void onComplete();
    }

    public interface SetReviewListener {
        void onComplete();
    }

    public void getReviews(Date lastUpdateDate, GetReviewsListener listener) {
        db.collection(Review.COLLECTION_NAME).whereGreaterThan("updateDate", lastUpdateDate)
                .get()
                .addOnCompleteListener(task -> {
                    List<Review> list = new LinkedList<Review>();
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Review review = Review.fromJSON(doc.getData(), doc.getId());

                            if (review != null) {
                                list.add(review);
                            }
                        }
                    }
                    listener.onComplete(list);
                });
    }

    public void deleteReview(String reviewKey, DeleteReviewListener listener) {
        Map<String,Object> updates = new HashMap<>();
        updates.put("isDeleted", true);
        updates.put("updateDate", new Date());

        db.collection(Review.COLLECTION_NAME)
                .document(reviewKey)
                .update(updates)
                .addOnSuccessListener(success -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> {
                    listener.onComplete();
                });
    }

    public void setReview(Review review, String documentId, SetReviewListener listener) {
        Map<String, Object> jsonReview = review.toJSON();

        if (documentId.equals("")) {
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            CollectionReference reviewsRef = rootRef.collection(Review.COLLECTION_NAME);
            documentId = reviewsRef.document().getId();
        }

        db.collection(Review.COLLECTION_NAME)
                .document(documentId)
                .set(jsonReview)
                .addOnSuccessListener(success -> {
                    listener.onComplete();
                })
                .addOnFailureListener(e -> {
                    listener.onComplete();
                });
    }

//    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener, String location) {
//        StorageReference storageRef = storage.getReference();
//        StorageReference imgRef = storageRef.child(location + imageName);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = imgRef.putBytes(data);
//        uploadTask.addOnFailureListener(exception -> listener.onComplete(null))
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        imgRef.getDownloadUrl().addOnSuccessListener(uri -> {
//                            Uri downloadUrl = uri;
//                            listener.onComplete(downloadUrl.toString());
//                        });
//                    }
//                });
//    }
//
//    public void deleteImage(String movieImageUrl, DeleteImageListener listener) {
//        StorageReference storageReference = storage.getReference();
//        StorageReference imageRef = storageReference.getStorage().getReferenceFromUrl(movieImageUrl);
//        imageRef.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                listener.onComplete();
//            }
//        });
//    }

    public boolean isSignedIn() {
        FirebaseUser currentUser = auth.getCurrentUser();
        return (currentUser != null);
    }

    public String getUserEmail() {
        return auth.getCurrentUser().getEmail();
    }

    public void getUserDetails(GetUserDetailsListener listener) {
        String userEmail = getUserEmail();

        db.collection(User.COLLECTION_NAME)
                .document(userEmail)
                .get()
                .addOnCompleteListener(task -> {
                    User user = new User();
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        user = User.fromJSON(document.getData(), document.getId());
                    }
                    listener.onComplete(user);
                });
    }

    public void register(String email, String password, Register listener) {
        if (email.equals("") ||
                password.equals("")) {
            listener.onFailed("Email and password are required");
            return;
        }
        if (password.length() < 6) {
            listener.onFailed("The password must be at least 6 characters long");
            return;
        }
        auth.createUserWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listener.onSuccess();

                        } else {
                            // If sign in fails, display a message to the user.
                            listener.onFailed("Register failed: " + task.getException());
                        }
                    }
                });
    }

    public void updatePassword(String password, UpdatePassword listener) {
        if (password.equals("")) {
            listener.onFailed("New password is required");
            return;
        }
        if (password.length() < 6) {
            listener.onFailed("The password must be at least 6 characters long");
            return;
        }
        auth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    listener.onSuccess();
                } else {
                    listener.onFailed("Update password failed: " + task.getException());
                }
            }
        });
    }

    public void setUserDetails(String email, String firstName, String lastName, String imageUrl, RegisterDetails listener) {
        Map<String, Object> jsonReview = new HashMap<>();
        jsonReview.put("firstName", firstName);
        jsonReview.put("lastName", lastName);
        jsonReview.put("imageUrl", imageUrl);

        // Sign in success, update UI with the signed-in user's information
        db.collection("users")
                .document(email)
                .set(jsonReview)
                .addOnSuccessListener(success -> {
                    listener.onSuccess();
                });
    }

    public void signIn(String email, String password, SignIn listener) {
        auth.signInWithEmailAndPassword(email.toString(), password.toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            listener.onSuccess();
                        } else {
                            // If sign in fails, display a message to the user.
                            listener.onFailed();
                        }
                    }
                });
    }

    public void logout() {
        auth.signOut();
    }
}
