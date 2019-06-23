package com.example.skeleton.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.skeleton.R;
import com.example.skeleton.lib.RequestManager.RequestManager;
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
        if (Connectivity.isNetworkAvailable(MainActivity.this)) {
            RequestManager rm = RequestManager.builder(MainActivity.this)
                                              .setUrl("https://api.github.com/users/octocat/orgs")
                                              .setOnCompletion(new RunnableArgs() {
                                                  @Override
                                                  public void run() {

                                                  }

                                                  @Override
                                                  public void run(Object ...args) {
                                                      JSONObject j = (JSONObject) args[0];
                                                      run();
                                                  }

                                              })
                                              .setOnError(new RunnableArgs() {
                                                  @Override
                                                  public void run() {
                                                      Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT)
                                                              .show();
                                                  }
                                              });
            rm.send(new JSONObject());
        }
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
