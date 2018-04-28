package com.isoftston.issuser.conchapp.views.security;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.corelibs.base.BaseActivity;
import com.corelibs.base.BasePresenter;
import com.corelibs.utils.IMEUtil;
import com.corelibs.utils.ToastMgr;
import com.isoftston.issuser.conchapp.R;
import com.isoftston.issuser.conchapp.constants.Constant;
import com.isoftston.issuser.conchapp.model.bean.AddYHBean;
import com.isoftston.issuser.conchapp.model.bean.OrgBean;
import com.isoftston.issuser.conchapp.model.bean.SafeListBean;
import com.isoftston.issuser.conchapp.model.bean.SecuritySearchBean;
import com.isoftston.issuser.conchapp.model.bean.YhlyBean;
import com.isoftston.issuser.conchapp.presenter.SecurityPresenter;
import com.isoftston.issuser.conchapp.utils.DateUtils;
import com.isoftston.issuser.conchapp.utils.ToastUtils;
import com.isoftston.issuser.conchapp.utils.Tools;
import com.isoftston.issuser.conchapp.views.interfaces.SecuryView;
import com.isoftston.issuser.conchapp.views.work.NewWorkActivity;
import com.isoftston.issuser.conchapp.weight.CustomDatePicker;
import com.isoftston.issuser.conchapp.weight.InputView;
import com.isoftston.issuser.conchapp.weight.NavBar;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by issuser on 2018/4/10.
 */

public class AddHiddenTroubleActivity extends BaseActivity<SecuryView,SecurityPresenter> implements SecuryView  {

    @Bind(R.id.nav)
    NavBar navBar;
    @Bind(R.id.input_trouble_name)
    InputView input_trouble_name;
    @Bind(R.id.tv_yh_type)
    TextView tv_yh_type;
    @Bind(R.id.tv_check_people)
    TextView tv_check_people;
    @Bind(R.id.input_place)
    InputView input_place;
    @Bind(R.id.input_position)
    InputView input_position;

    @Bind(R.id.et_yh_description)
    EditText et_description;
    @Bind(R.id.rl_description)
    RelativeLayout rl_description;
    @Bind(R.id.tv_start_time)
    TextView tv_start_time;
    @Bind(R.id.tv_end_time)
    TextView tv_end_time;
    @Bind(R.id.ll_description)
    LinearLayout ll_description;
    @Bind(R.id.tv_illegal_describ_title)
    TextView tv_illegal_describ_title;
    @Bind(R.id.tv_illegal_descibe_content)
    TextView tv_illegal_descibe_content;
    @Bind(R.id.spinner)
    Spinner mSpinner;
    @Bind(R.id.spinner1)
    Spinner comSpinner;
    @Bind(R.id.spinner3)
    Spinner fromSpinner;
    @Bind(R.id.male_rb)
    RadioButton male_rb;
    @Bind(R.id.famale_rb)
    RadioButton famale_rb;
    @Bind(R.id.fix_yes)
    RadioButton fix_yes;
    @Bind(R.id.fix_no)
    RadioButton fix_no;
    public String startTime,endTime;
    private List<String> findCompanyList=new ArrayList<>();
    private List<String> checkCompanyList=new ArrayList<>();
    private List<String> fromList=new ArrayList<>();
    private List<String> fromListId=new ArrayList<>();
    private Context context =AddHiddenTroubleActivity.this;
    //0隐患 1违章
    private String type;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> comAdapter;
    private ArrayAdapter<String> fromAdapter;
    private String find_company;
    private String yh_company;
    private String yh_from;
    private String yh_from_id;
    private String yh_lx_id;
    private String yh_grade=null;
    private String fix;
    private List<OrgBean> org=new ArrayList<>();
    private String find_company_id;
    private String yh_company_id;
    private String check_peole_id;

    public static Intent getLauncher(Context context){
        Intent intent =new Intent(context,AddHiddenTroubleActivity.class);
        return intent;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_hidden_trouble;
    }

    @Override
    protected void init(Bundle savedInstanceState) {

        navBar.setColorRes(R.color.white);
        navBar.setTitleColor(getResources().getColor(R.color.black));
        navBar.showBack(2);
        setBarColor(getResources().getColor(R.color.transparent_black));

            //隐患
            navBar.setNavTitle(getString(R.string.hidden_trouble));
            input_trouble_name.setInputText(getString(R.string.hidden_trouble_detail_name),null);

            input_place.setInputText(getString(R.string.hidden_trouble_place),null);
            input_position.setInputText(getString(R.string.hidden_trouble_position),null);



//        input_find_company.setInputText(getString(R.string.hidden_trouble_find_company),null);
        starttime= Tools.getCurrentTime();
        endtime=Tools.getCurrentTime();
        tv_start_time.setText(starttime);
        tv_end_time.setText(endtime);
        et_description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    if(!TextUtils.isEmpty(et_description.getText().toString())){
                        tv_illegal_descibe_content.setText(et_description.getText().toString());
                    }else{
                        tv_illegal_descibe_content.setText("");
                    }
                    et_description.setVisibility(View.GONE);
                    ll_description.setVisibility(View.VISIBLE);
                    rl_description.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_illegal_describ_title.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        et_description.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId== EditorInfo.IME_ACTION_DONE||actionId==EditorInfo.IME_ACTION_SEARCH){
                    if(!TextUtils.isEmpty(et_description.getText().toString())){

                        tv_illegal_descibe_content.setText(et_description.getText().toString());
                    }else{
                        tv_illegal_descibe_content.setText("");
                    }
                    et_description.setVisibility(View.GONE);
                    ll_description.setVisibility(View.VISIBLE);
                    rl_description.setBackgroundColor(getResources().getColor(R.color.transparent));
                    tv_illegal_describ_title.setTextColor(getResources().getColor(R.color.black));
                    IMEUtil.closeIME(et_description,context);
                    return true;
                }
                return false;
            }
        });
        presenter.getCompanyChoiceList();
        initSpinner();
        //样式为原安卓里面有的android.R.layout.simple_spinner_item，让这个数组适配器装list内容。


    }

    private void initSpinner() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, findCompanyList);
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        mSpinner.setAdapter(adapter);
        mSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                find_company = adapter.getItem(i).toString();
                find_company_id = org.get(i).getID_();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        comAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, checkCompanyList);
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        comAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        comSpinner.setAdapter(comAdapter);
        comSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yh_company = comAdapter.getItem(i).toString();
                yh_company_id = org.get(i).getID_();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
        fromAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fromList);
        //2.为适配器设置下拉菜单样式。adapter.setDropDownViewResource
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //3.以上声明完毕后，建立适配器,有关于sipnner这个控件的建立。用到myspinner
        fromSpinner.setAdapter(fromAdapter);
        fromSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                yh_from = fromAdapter.getItem(i).toString();
                yh_from_id = fromListId.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

    }

    @Override
    protected SecurityPresenter createPresenter() {
        return new SecurityPresenter();
    }

    @OnClick(R.id.rl_check_people)
    public void choiceCheckPeople(){
        startActivityForResult(ChoiceCheckPeopleActivity.getLaucnher(context,6),100);
    }
    @OnClick(R.id.ll_yh_type)
    public void choiceType(){
        startActivityForResult(ChoiceTypeActivity.getLaucnher(context,1),110);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            if(resultCode==100){
                String result=data.getStringExtra(Constant.CHECK_PEOPLE);
                check_peole_id = data.getStringExtra(Constant.CHECK_PEOPLE_ID);
                if(!TextUtils.isEmpty(result)){
                    tv_check_people.setText(result);
                }
            }
        }else if(requestCode==110){
            if(resultCode==10){
                map= (HashMap<String, String>) data.getSerializableExtra(Constant.TEMP_PIC_LIST);
                StringBuilder builder = new StringBuilder();
                for (String path : map.values()){
                    builder.append(path);
                    builder.append(",");
                }
                picString = builder.toString();
            }else if (resultCode==104){
                String name=data.getStringExtra(Constant.CHECK_PEOPLE);
                String id=data.getStringExtra(Constant.CHECK_PEOPLE_ID);
                tv_yh_type.setText(name);
                yh_lx_id = id;
            }
        }
    }
    private HashMap<String, String> map =new HashMap<>();
    private String picString;

    @OnClick(R.id.rl_photo)
    public void goPhoto(){
        //进入照片选择界面
        startActivityForResult(ChoicePhotoActivity.getLauncher(context,"0",map),110);
    }


    @OnClick(R.id.ll_description)
    public void choiceDescription(){
        ll_description.setVisibility(View.GONE);
        et_description.setVisibility(View.VISIBLE);
        et_description.setFocusable(true);
        et_description.setFocusableInTouchMode(true);
        et_description.requestFocus();
        rl_description.setBackgroundResource(R.drawable.ll_input_selector_bg);
        tv_illegal_describ_title.setTextColor(getResources().getColor(R.color.white));
    }

    @OnClick(R.id.ll_confirm)
    public void confirmInfo(){
        //提交新增信息 暂时结束页面
        String yh_name=input_trouble_name.getContent().trim();
        String startTimeStr = tv_start_time.getText().toString().trim();
        long startTime = TextUtils.isEmpty(String.valueOf(startTimeStr))? 0 : DateUtils.getDateToLongMS(startTimeStr);
        String endTimeStr = tv_end_time.getText().toString().trim();
        long endTime = TextUtils.isEmpty(String.valueOf(endTimeStr))? 0 : DateUtils.getDateToLongMS(endTimeStr);
        String check_people=tv_check_people.getText().toString();
        if (male_rb.isChecked()){
            //yh_grade = male_rb.getText().toString();
            yh_grade="ZDYH";
        }else if (famale_rb.isChecked()){
            //yh_grade=famale_rb.getText().toString();
            yh_grade="YBYH";
        }
        String yh_address=input_place.getContent().trim();
        String yh_position=input_position.getContent().trim();
        String yh_type=tv_yh_type.getText().toString();
        String yh_describle=et_description.getText().toString().trim();
        if (fix_yes.isChecked()){
            fix = fix_yes.getText().toString();
        }else if (fix_no.isChecked()){
            fix=fix_no.getText().toString();
        }
        if (TextUtils.isEmpty(yh_name)||TextUtils.isEmpty(find_company)||TextUtils.isEmpty(yh_company)||
                TextUtils.isEmpty(check_people)||TextUtils.isEmpty(yh_grade)||TextUtils.isEmpty(yh_address)||
                TextUtils.isEmpty(yh_position)||TextUtils.isEmpty(yh_from)||TextUtils.isEmpty(yh_type)
                ||TextUtils.isEmpty(fix)||TextUtils.isEmpty(yh_describle)){
            ToastMgr.show(R.string.input_all_message);
            return;
        }

        AddYHBean bean=new AddYHBean();
        bean.setYhmc(yh_name);
//        bean.setGsId("1");
        bean.setJcdwid(find_company_id);
        bean.setJcdwmc(find_company);
        bean.setSjdwid(yh_company_id);
        bean.setSjdwmc(yh_company);
        //bean.setYhly(yh_from);
        bean.setYhly(yh_from_id);
        bean.setFxrmc(check_people);
        bean.setFxrId(check_peole_id);
        bean.setFxrq(startTime);
        bean.setCjsj(endTime);
        //bean.setYhlx(yh_type);
        bean.setYhlx(yh_lx_id);
        bean.setYhjb(yh_grade);
        bean.setYhdd(yh_address);
        bean.setYhbw(yh_position);
        if (fix.equals("是")){
            bean.setSfxczg("1");
        }else {
            bean.setSfxczg("0");
        }
        bean.setYhms(yh_describle);
        bean.setTplj(picString);
//        bean.setKnzchg("1");
        presenter.addYHMessage(bean);
    }

    private boolean isDateOneBigger(String beginDateTime, String endDateTime) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(beginDateTime);
            dt2 = sdf.parse(endDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if ((dt1.getTime() - dt2.getTime())>0) {
            isBigger = true;
        } else  {
            isBigger = false;
        }
        return isBigger;
    }

    @OnClick(R.id.tv_start_time)
    public void choiceStartTime(){
        showDatePickerDialog(tv_start_time,1);
    }

    @OnClick(R.id.tv_end_time)
    public void choiceEndTime(){
        showDatePickerDialog(tv_end_time,2);
    }

    private String starttime;
    private String endtime;

    private void showDatePickerDialog(final TextView textView, final int i) {


        CustomDatePicker customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {



            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                try {
                    if (i==1){
                        starttime = DateUtils.format_yyyy_MM_dd_HH_mm.format(DateUtils.format_yyyy_MM_dd_HH_mm.parse(time));
                        endtime=tv_end_time.getText().toString();
                    }else {
                        starttime = tv_start_time.getText().toString();
                        endtime=DateUtils.format_yyyy_MM_dd_HH_mm.format(DateUtils.format_yyyy_MM_dd_HH_mm.parse(time));
                    }
                    if (isDateOneBigger(starttime,endtime)){
                        Toast.makeText(AddHiddenTroubleActivity.this,R.string.hidden_info,Toast.LENGTH_SHORT).show();
                        return;
                    }
                    textView.setText(DateUtils.format_yyyy_MM_dd_HH_mm.format(DateUtils.format_yyyy_MM_dd_HH_mm.parse(time)));
//                    textView.setTextColor(getResources().getColor(R.color.black));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, "1970-01-01 00:00", "2099-12-12 00:00"); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(true); // 不显示时和分
        //customDatePicker.showYearMonth();
        customDatePicker.setIsLoop(false); // 不允许循环滚动
        //customDatePicker.show(dateText.getText().toString() + " " + timeText.getText().toString());
        customDatePicker.show(DateUtils.format_yyyy_MM_dd_HH_mm.format(new Date()));
    }

    @Override
    public void onLoadingCompleted() {

    }

    @Override
    public void onAllPageLoaded() {

    }

    @Override
    public void addSuccess() {
        ToastMgr.show(getString(R.string.submit_success));
        finish();

    }

    @Override
    public void getSafeListSuccess(SafeListBean data) {

    }

    @Override
    public void addFailed() {
        finish();
    }

    @Override
    public void getSafeChoiceList(SecuritySearchBean bean ) {
        checkCompanyList.clear();
        findCompanyList.clear();
        org = bean.ORG;
        for (OrgBean orgBean: org){
            checkCompanyList.add(orgBean.getORG_NAME_());
            findCompanyList.add(orgBean.getORG_NAME_());
        }
        List<YhlyBean> YHLY=bean.YHLY;

        for (YhlyBean yhlyBean:YHLY){
            fromList.add(yhlyBean.getNAME_());
            fromListId.add(yhlyBean.getCODE_());
        }
        adapter.notifyDataSetChanged();
        fromAdapter.notifyDataSetChanged();
        comAdapter.notifyDataSetChanged();

    }

    @Override
    public void getOrgId(String orgId) {

    }
}
