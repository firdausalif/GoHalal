package id.ac.unj.gohalal.Fragments;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import id.ac.unj.gohalal.Helper.CustomToast;
import id.ac.unj.gohalal.MainActivity;
import id.ac.unj.gohalal.R;
import id.ac.unj.gohalal.Helper.Utils;

import android.app.ProgressDialog;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.ac.unj.gohalal.Helper.JSONParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by SuperNova's on 25/05/2017.
 */

public class SignUp_Fragment extends Fragment implements OnClickListener {
	String URL= "http://gohalal.pe.hu/GoHalal/index.php/Register";
	JSONParser jsonParser=new JSONParser();

	private static View view;
	private static EditText username, emailId, mobileNumber,
			password, confirmPassword;
	private static TextView login;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private static LinearLayout signupLayout;
	private static Animation shakeAnimation;
	ProgressDialog pDialog;

	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		initViews();
		setListeners();
		return view;
	}

	// Initialize all views
	private void initViews() {
		username = (EditText) view.findViewById(R.id.userName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		password = (EditText) view.findViewById(R.id.password);
		confirmPassword = (EditText) view.findViewById(R.id.confirmPassword);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
		signupLayout = (LinearLayout) view.findViewById(R.id.signup_layout);

		// Load ShakeAnimation
		shakeAnimation = AnimationUtils.loadAnimation(getActivity(),
				R.anim.shake);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.xml.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}

	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:
			// Call checkValidation method
			checkValidation();
			break;
		case R.id.already_user:
			// Replace login fragment
			new MainActivity().replaceLoginFragment();
			break;
		}

	}

	private void checkValidation() {

		String getUserName = username.getText().toString();
		String getEmailId = emailId.getText().toString();
		String getMobileNumber = mobileNumber.getText().toString();
		String getPassword = password.getText().toString();
		String getConfirmPassword = confirmPassword.getText().toString();

		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(getEmailId);

		if (getUserName.equals("") || getUserName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getMobileNumber.equals("") || getMobileNumber.length() == 0
				|| getPassword.equals("") || getPassword.length() == 0
				|| getConfirmPassword.equals("")
				|| getConfirmPassword.length() == 0) {
			signupLayout.startAnimation(shakeAnimation);
			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");
		}else if (!m.find()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");
		}else if (!getConfirmPassword.equals(getPassword)) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");
		}else if (!terms_conditions.isChecked()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");
		}else if(getUserName.length() < 5 || getPassword.length() < 6){
			new CustomToast().Show_Toast(getActivity(), view,
					"Username/Password min 6 characters");
		}else {
            AttemptRegister attempRegister= new AttemptRegister();
            attempRegister.execute(getUserName, getEmailId, getPassword, getMobileNumber);
		}
	}

	private class AttemptRegister extends AsyncTask<String, String, JSONObject> {

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

			String username= args[0];
			String email = args[1];
			String password = args[2];
			String telp = args[3];

			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("username", username));
			params.add(new BasicNameValuePair("email",email));
			params.add(new BasicNameValuePair("password", password));
			params.add(new BasicNameValuePair("telp", telp));

			JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);
			return json;
		}

		protected void onPostExecute(JSONObject result) {
			try {
				if(result != null){
					int resultjson = result.getInt("success");
					if (resultjson == 1){
						pDialog.dismiss();
						new CustomToast().Show_Toast(getActivity(), view,
								result.getString("msg"));
						new MainActivity().replaceLoginFragment();
					}else{
						pDialog.dismiss();
						new CustomToast().Show_Toast(getActivity(), view,
								result.getString("msg"));
					}
				}else{
					pDialog.dismiss();
					new CustomToast().Show_Toast(getActivity(), view,
							"Cant register, try again!!");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
}
