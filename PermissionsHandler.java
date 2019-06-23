package com.example.skeleton.lib.services.permissions;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.skeleton.lib.runnables.RunnableArgs;

import java.util.ArrayList;
import java.util.List;

public class PermissionsHandler {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 100;
    private final Activity mActivity;
    private List<String> mPermissionsDenied;
    private final Runnable mOnDenied;
    private final Runnable mOnGranted;
    private final RunnableArgs mOnSuperPermission;
    private final Runnable mOnNoPermissions;

    public PermissionsHandler(@NonNull Activity activity, @NonNull Runnable onGranted, @NonNull Runnable onDenied, @NonNull RunnableArgs onSuper, @NonNull Runnable onNoPermissions) {
        mActivity = activity;
        mOnGranted = onGranted;
        mOnDenied  = onDenied;
        mOnSuperPermission = onSuper;
        mOnNoPermissions = onNoPermissions;
    }

    public void onCheckPermissions() {
        PackageInfo info = null;
        try {
            info = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            mOnNoPermissions.run();
        }
        revisarPermisos(info);
        pedirPermisos();
    }

    private void pedirPermisos() {
        if (mPermissionsDenied.isEmpty()) {
            mOnGranted.run();
        } else {
            String[] lista = new String[mPermissionsDenied.size()];
            ActivityCompat.requestPermissions(mActivity, mPermissionsDenied.toArray(lista), REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    private void revisarPermisos(PackageInfo info) {
        mPermissionsDenied = new ArrayList<>();
        String[] permissions = info.requestedPermissions;
        for (String p : permissions) {
            if (!isGranted(p)) {
                mPermissionsDenied.add(p);
            }
        }
    }

    private boolean isGranted(String permission) {
        return ContextCompat.checkSelfPermission(mActivity, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public void requestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean  granted     = true;
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            for (int i : grantResults) {
                granted = granted && (i == PackageManager.PERMISSION_GRANTED);
            }
            if (granted) {
                mOnGranted.run();
            } else {
                mOnDenied.run();
            }
        } else {
            mOnSuperPermission.run(requestCode, permissions, grantResults);
        }
    }
}
