package com.trigma.tiktok.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.internal.ShareConstants;
import com.trigma.tiktok.C1020R;
import com.trigma.tiktok.utils.CommonListeners.AlertCallBackInterface;
import com.trigma.tiktok.utils.CommonUtils;
import com.trigma.tiktok.utils.DialogPopUps;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

public class SocialLogin {
    public static final int FACEBOOK_LOGIN = 6;
    public static final int GOOGLE_LOGIN = 7;
    private Activity activity;
    private ImageView btnLoginViaFacebook;
    private CallbackManager callbackManager;
    private SocialLoginData loginData;

    class C11441 implements FacebookCallback<LoginResult> {

        class C11431 implements AlertCallBackInterface {
            C11431() {
            }

            public void neutralClick() {
            }
        }

        C11441() {
        }

        public void onSuccess(LoginResult loginResult) {
            Log.e("ACCESS TOKEN", loginResult.getAccessToken().getToken());
            SocialLogin.this.loginData = new SocialLoginData();
            SocialLogin.this.loginData.setAccess_token(loginResult.getAccessToken().getToken());
            SocialLogin.this.requestFacebookUserData(loginResult.getAccessToken());
        }

        public void onCancel() {
            Log.e("CANCEL", "CANCEL");
            SocialLogin.this.loginData = new SocialLoginData();
        }

        public void onError(FacebookException exception) {
            Log.e("ERROR", "ERROR");
            try {
                if (!CommonUtils.isConnectedToInternet(SocialLogin.this.activity)) {
                    DialogPopUps.alertPopUp(SocialLogin.this.activity, SocialLogin.this.activity.getResources().getString(C1020R.string.check_your_network), SocialLogin.this.activity.getResources().getString(C1020R.string.ok), new C11431());
                }
                Log.v("ERROR", exception.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class C11452 implements OnClickListener {
        C11452() {
        }

        public void onClick(View view) {
            LoginManager.getInstance().logInWithReadPermissions(SocialLogin.this.activity, Arrays.asList(new String[]{"email"}));
        }
    }

    class C11463 implements GraphJSONObjectCallback {
        C11463() {
        }

        public void onCompleted(JSONObject object, GraphResponse response) {
            try {
                Log.v("GRAPH RESPONSE", response.toString());
                Log.v("JSON OBJECT", object.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                SocialLogin.this.loginData.setSocialUserID(SocialLogin.this.getValue(ShareConstants.WEB_DIALOG_PARAM_ID, object));
                SocialLogin.this.loginData.setBirthday_date(SocialLogin.this.getValue("birthday", object));
                SocialLogin.this.loginData.setSex(SocialLogin.this.getValue("gender", object));
                String name = SocialLogin.this.getValue("name", object);
                SocialLogin.this.loginData.setEmail(SocialLogin.this.getValue("email", object));
                SocialLogin.this.loginData.setFirst_name(SocialLogin.this.getValue("first_name", object));
                SocialLogin.this.loginData.setLast_name(SocialLogin.this.getValue("last_name", object));
                SocialLogin.this.loginData.setAbout_me(SocialLogin.this.getValue("about", object));
                SocialLogin.this.returnFacebookUserData();
            } catch (JSONException e2) {
            }
        }
    }

    public SocialLogin(Activity activity) {
        this.activity = activity;
    }

    public CallbackManager loginViaFacebook(int viewId) {
        this.callbackManager = Factory.create();
        LoginManager.getInstance().registerCallback(this.callbackManager, new C11441());
        this.btnLoginViaFacebook = (ImageView) this.activity.findViewById(viewId);
        this.btnLoginViaFacebook.setOnClickListener(new C11452());
        return this.callbackManager;
    }

    public void requestFacebookUserData(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(token, new C11463());
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id, name, email, gender, birthday, about, first_name, last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private String getValue(String key, JSONObject jObj) throws JSONException {
        if (jObj.has(key)) {
            return jObj.getString(key);
        }
        return "";
    }

    private void returnFacebookUserData() {
        try {
            this.loginData.setPic_big("https://graph.facebook.com/" + this.loginData.getSocialUserID() + "/picture?width=640&height=640");
            this.loginData.setUserType("F");
            if (this.activity instanceof OnSocialLoginListener) {
                ((OnSocialLoginListener) this.activity).onSocialLogin(6, this.loginData);
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.loginData.setPic_big("");
            this.loginData.setUserType("F");
            if (this.activity instanceof OnSocialLoginListener) {
                ((OnSocialLoginListener) this.activity).onSocialLogin(6, this.loginData);
            }
        }
    }
}
