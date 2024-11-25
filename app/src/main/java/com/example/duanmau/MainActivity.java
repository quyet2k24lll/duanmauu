package com.example.duanmau;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.duanmau.dao.SachDAO;
import com.example.duanmau.dao.ThuThuDAO;
import com.example.duanmau.fragment.QLLoaiSachFragment;
import com.example.duanmau.fragment.QLPhieuMuonFragment;
import com.example.duanmau.fragment.QLSachFragment;
import com.example.duanmau.fragment.QLThanhVienFragment;
import com.example.duanmau.fragment.ThongKeDoanhThuFragment;
import com.example.duanmau.fragment.ThongKeTop10Fragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolBar);
        FrameLayout frameLayout = findViewById(R.id.frameLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        drawerLayout = findViewById(R.id.drawerLayout);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                if (item.getItemId() == R.id.mQLPhieuMuon){
                    fragment = new QLPhieuMuonFragment();
                } else if (item.getItemId() == R.id.mQLLoaiSach){
                    fragment = new QLLoaiSachFragment();
                } else if (item.getItemId() == R.id.mQLSach){
                    fragment = new QLSachFragment();
                }
                else if (item.getItemId() == R.id.mQLThanhVien){
                    fragment = new QLThanhVienFragment();
                }
                else if (item.getItemId() == R.id.mTop10){
                    fragment = new ThongKeTop10Fragment();
                } else if (item.getItemId() == R.id.mDoanhThu){
                    fragment = new ThongKeDoanhThuFragment();
                } else if (item.getItemId() == R.id.mThoat){
                    Intent intent = new Intent(MainActivity.this, DangNhapActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else if (item.getItemId() == R.id.mDoiMatKhau){
                    showDialogDoiMatKhau();
                }
                if (fragment != null){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout,fragment)
                            .commit();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    toolbar.setTitle(item.getTitle());
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
    private void showDialogDoiMatKhau(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_doimatkhau,null);
        EditText edtOldPass = view.findViewById(R.id.edtPassOld);
        EditText edtNewPass = view.findViewById(R.id.edtNewPass);
        EditText edtReNewPass = view.findViewById(R.id.edtReNewPass);

        builder.setView(view);

        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("Cập nhật", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String oldPass = edtOldPass.getText().toString();
                String newPass = edtNewPass.getText().toString();
                String reNewPass = edtReNewPass.getText().toString();
                if (newPass.equals(reNewPass)){
                    SharedPreferences sharedPreferences = getSharedPreferences("THONGTIN", MODE_PRIVATE);
                    String matt = sharedPreferences.getString("matt","");
                    // cập nhật
                    ThuThuDAO thuThuDAO = new ThuThuDAO(MainActivity.this);
                    boolean check = thuThuDAO.capNhatMatKhau(matt,oldPass,newPass);
                    if (check){
                        Toast.makeText(MainActivity.this, "Cập nhật mật khẩu", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this,DangNhapActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "Thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this,"Nhập mật khẩu không trùng với nhau ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}