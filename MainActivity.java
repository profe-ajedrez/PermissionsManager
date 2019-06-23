package com.example.skeleton.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skeleton.R;
import com.example.skeleton.lib.runnables.RunnableArgs;
import com.example.skeleton.lib.services.connectivity.Connectivity;
import com.example.skeleton.lib.services.permissions.PermissionsHandler;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private PermissionsHandler mPermissionHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPermissionHandler = getPermissionHandler();
        mPermissionHandler.onCheckPermissions();
    }

    public PermissionsHandler getPermissionHandler() {
        return new PermissionsHandler(
                MainActivity.this,
                new Runnable() {
                    @Override
                    public void run() {
                        iniciar();
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        terminar();
                    }
                },
                new RunnableArgs() {
                    @Override
                    public void run() {
                        int requestCode = (int) mArgs[0];
                        String[] permissions = (String[]) mArgs[1];
                        int[] grantResults = (int[]) mArgs[3];
                        MainActivity.super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {

                    }
                }
        );
    }


    private void iniciar() {
        Toast.makeText(MainActivity.this, "Tenemos los permisos. adios", Toast.LENGTH_SHORT)
                .show();
    }

    private void terminar() {
        Toast.makeText(MainActivity.this, "Necesitamos los permisos. adios", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull final int[] grantResults) {
        mPermissionHandler.requestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );
    }
}
