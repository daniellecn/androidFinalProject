package com.example.finalproject.Model;

/**
 * Created by Danielle Cohen on 17/01/2017.
 */

public class ModelFirebase {

    public ModelFirebase() {
    }

    public void userLogIn(User user, Model.LogInListener listener) {
        UserFirebase.userLogIn(user, listener);
    }

    public void userSignUp(final User user, final Model.SuccessListener listener) {
        UserFirebase.userSignUp(user, new Model.SignUpListener() {
            @Override
            public void onComplete(boolean isExist) {
                if (isExist) {
                    listener.onResult(false);
                } else {
                    UserFirebase.addUser(user);
                    listener.onResult(true);
                }
            }
        });
    }

    public void addDessert(Dessert dessert, Model.SuccessListener listener) {
        // Add the dessert to firebase databace
        DessertFirebase.addDessert(dessert, listener);
    }

    public void getDessertsFromDate(double lastUpdateDate, final Model.GetAllDessertsListener listener) {
        DessertFirebase.getDessertsFromDate(lastUpdateDate, listener);
    }

    public void getDessertById(double id, final Model.GetDessertListener listener) {
        DessertFirebase.getDessertById(id, listener);
    }

    public void deleteDessert(int id, String url, Model.SuccessListener listener) {
        DessertFirebase.deleteDessert(id, listener);
        ImageFirebase.deleteRemoteImage(url, listener);
    }

    public void isUserNameExist(final String userName, Model.SuccessListener listener) {
        UserFirebase.isUserNameExist(userName, listener);
    }

    public void isUserMailExist(final String userMail, Model.SuccessListener listener) {
        UserFirebase.isUserMailExist(userMail, listener);
    }
}
