package com.example.recorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private static TextView t;
    private static Button b;
    private TcpClient tc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissons();
        t = (TextView)findViewById(R.id.textView5);
        b = findViewById(R.id.button);
        t.setMovementMethod(new ScrollingMovementMethod());
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                tc = new TcpClient(Settings.IP_ADDRESS, Settings.PORT);
                tc.start();
                b.setEnabled(false);
            }
        });
    }

    public static void addText(String s){
        t.append("\n"+s);
    }

    private void checkPermissons() {
        String[] permissions = {Manifest.permission.RECORD_AUDIO};
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean perimissionFlas = false;
            for (String permissionStr : permissions) {
                // 检查该权限是否已经获取
                int per = ContextCompat.checkSelfPermission(this, permissionStr);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (per != PackageManager.PERMISSION_GRANTED) {
                    perimissionFlas = true;
                }
            }
            if (perimissionFlas) {
                // 如果有权限没有授予允许，就去提示用户请求授权
                ActivityCompat.requestPermissions(this, permissions, 321);
            }
        }
    }

}