package id.ac.unj.gohalal.Fragments;

import java.util.ArrayList;

import id.ac.unj.gohalal.MapsActivity;
import id.ac.unj.gohalal.helper.CustomToast;
import id.ac.unj.gohalal.R;
import id.ac.unj.gohalal.helper.Utils;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.ac.unj.gohalal.helper.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class Login_Fragment extends Fragment implements OnClickListener {
	private static View view;

	String URL= "http://gohalal.pe.hu/test/login.php";
	JSONParser jsonParser =new JSONParser();
	ProgressDialog pDialog;

	private static EditText username, password;
	private static Button loginButton;
	private static TextView forgotPassword, signUp;
	private static CheckBox show_hide_password;
	private static LinearLayout loginLayout;
	private static Animation shakeAnimation;
	private static FragmentManager fragmentManager;

	public Login_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.login_layout, container, false);
		initViews();
		setListeners();
		return view;

	}

	// Initiate Views
	private void initViews() {
		fragmentManager = getActivity().getSupportFragmentManager();
		username = (EditText) view.findViewById(R.id.login_username);
		password = (EditText) view.findViewById(R.id.login_password);
		loginButton = (Button) view.findViewById(R.id.loginBtn);
		forgotPassword = (TextView) view.findViewById(R.id.forgot_password);
		signUp = (TextView) view.findViewById(R.id.createAccount);
		show_hide_password = (CheckBox) view
				.findViewById(R.id.show_hide_password);
		loginLayout = (LinearLayout) view.findViewById(R.id.login_layout);

		// Load ShakeAnimation
		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			forgotPassword.setTextColor(csl);
			show_hide_password.setTextColor(csl);
			signUp.setTextColor(csl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Set Listeners
	private void setListeners() {
		loginButton.setOnClickListener(this);
		forgotPassword.setOnClickListener(this);
		signUp.setOnClickListener(this);

		// Set check listener over checkbox for showing and hiding password
		show_hide_password
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton button,
							boolean isChecked) {

						// If it is checkec then show password else hide
						// password
						if (isChecked) {
							show_hide_password.setText(R.string.hide_pwd);// change
																			// checkbox
																			// text
							password.setInputType(InputType.TYPE_CLASS_TEXT);
							password.setTransformationMethod(HideReturnsTransformationMethod
									.getInstance());// show password
						} else {
							show_hide_password.setText(R.string.show_pwd);// change
																			// checkbox
																			// text

							password.setInputType(InputType.TYPE_CLASS_TEXT
									| InputType.TYPE_TEXT_VARIATION_PASSWORD);
							password.setTransformationMethod(PasswordTransformationMethod
									.getInstance());// hide password

						}

					}
				});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.loginBtn:
			checkValidation();
			break;

		case R.id.forgot_password:

			// Replace forgot password fragment with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer,
							new ForgotPassword_Fragment(),
							Utils.ForgotPassword_Fragment).commit();
			break;

		case R.id.createAccount:

			// Replace signup frgament with animation
			fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.right_enter, R.anim.left_out)
					.replace(R.id.frameContainer, new SignUp_Fragment(),
							Utils.SignUp_Fragment).commit();
		}

	}

	// Check Validation before login
	private void checkValidation() {
		// Get email id and password
		String getPassword = password.getText().toString();
		String getUserName = username.getText().toString();

		 //Check for both field is empty or not
		if (getUserName.equals("") || getUserName.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0) {
			loginLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,
					"Enter your credentials.");
		}
		//do Login
		else{
			AttemptLogin attemptLogin= new AttemptLogin();
			attemptLogin.execute(getUserName, getPassword)	;
		}

	}

	private class AttemptLogin extends AsyncTask<String, String, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("Please Wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... args) {

			String password = args[1];
			String username= args[0];

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("password", password));


			JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);
			return json;
		}

		protected void onPostExecute(JSONObject result) {
			// dismiss the dialog once product deleted
			//Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
			try {
				int resultjson = result.getInt("success");
				if (resultjson == 1) {
					pDialog.dismiss();

					Toast.makeText(getActivity(), result.getString("message"),Toast.LENGTH_LONG).show();
					Intent intent = new Intent(getActivity(), MapsActivity.class);
					startActivity(intent);
				} else {
					pDialog.dismiss();
					new CustomToast().Show_Toast(getActivity(), view,
							result.getString("message"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

}
