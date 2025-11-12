package com.example.zba.data;

import android.os.Build;

import com.example.zba.models.PasswordItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PasswordRepository {
    public static final List <PasswordItem> passwordList = new ArrayList<>();

    public static List<PasswordItem> getPasswords() {
        return passwordList;
    }
    public static void addPassword(PasswordItem item){
        passwordList.add(item);
    }

    public static void removePassword(String id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            passwordList.removeIf(item -> item.getId().equals(id));
        }else {
            Iterator<PasswordItem> iterator = passwordList.iterator();
            while (iterator.hasNext()) {
                PasswordItem item = iterator.next();
                if (item.getId().equals(id)) {
                    iterator.remove();
                }
            }
        }
    }
    public static boolean updatePassword(String id, String newAppName, String newUsername, String newPassword) {
        for (PasswordItem item : passwordList) {
            if (item.getId().equals(id)) {
                item.setAppName(newAppName);
                item.setUserName(newUsername);
                item.setPassword(newPassword);
                return true;
            }
        }
        return false;
    }

    public static void clear(){
        passwordList.clear();
    }
}
