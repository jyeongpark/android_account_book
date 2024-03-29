package com.example.food_account.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food_account.Information;
import com.example.food_account.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class InformationActivity extends AppCompatActivity {
    private static final String TAG = "InformationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        findViewById(R.id.button_member_info).setOnClickListener(onClickListener);
    }
    @Override public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch  (v.getId()){
                case R.id.button_member_info:
                    profileUpdate();
                    break;

            }
        }
    };
    private void profileUpdate() {
        String nickname = ((EditText) findViewById(R.id.editText_nickname)).getText().toString();
        int food_expense = Integer.parseInt(((EditText) findViewById(R.id.editText_food_expense)).getText().toString());

        if (nickname.length() > 0 && food_expense>0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            String uid = user.getUid();
            Information information = new Information(nickname,food_expense,uid);
            if(user !=null) {
                db.collection("users").document(user.getUid()).set(information)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                startToast("회원 정보 등록에 성공했습니다.");
                                startLoginActivity();
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원 정보 등록에 실패했습니다.");
                            }
                        });
            }
        }else {

            startToast("회원정보를 입력해 주세요.");

        }
    }

    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();//토스트 메세지 보이기
    }

    private void startLoginActivity(){//로그인액티비티로 가기
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}