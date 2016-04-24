package com.sina.scm.data.DO;

public class NodeCase {
	
	private Integer id;
	private Integer node_def_id;
	private Integer pipeline_case_id;
	private String job_url;
	private String parameter;
	private String start_time;
	private String dev_test_result;
	private String dev_tester;
	private String qa_test_result;
	private String qa_tester;
	private String urgent_approver;
	private String urgent_approve_result;
	private String status;
	private String job_log_url;
	private String qa_test_time;
	private String dev_test_time;
	private String approve_time;
	private String end_time;
	private String name;
	private String type;	
	private String notification_mail_list;
    private String start_next_node;	
    private String test_type;    
    private String preview_test_result_url;    
    private String code_diff; 
    private String comments;    
    
	public String getCode_diff() {
		return code_diff;
	}
	public void setCode_diff(String code_diff) {
		this.code_diff = code_diff;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getPreview_test_result_url() {
		return preview_test_result_url;
	}
	public void setPreview_test_result_url(String preview_test_result_url) {
		this.preview_test_result_url = preview_test_result_url;
	}
	public String getTest_type() {
		return test_type;
	}
	public void setTest_type(String test_type) {
		this.test_type = test_type;
	}
	public String getStart_next_node() {
		return start_next_node;
	}
	public void setStart_next_node(String start_next_node) {
		this.start_next_node = start_next_node;
	}
	
	public String getNotification_mail_list() {
		return notification_mail_list;
	}
	public void setNotification_mail_list(String notification_mail_list) {
		this.notification_mail_list = notification_mail_list;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getNode_def_id() {
		return node_def_id;
	}
	public void setNode_def_id(Integer node_def_id) {
		this.node_def_id = node_def_id;
	}
	public Integer getPipeline_case_id() {
		return pipeline_case_id;
	}
	public void setPipeline_case_id(Integer pipeline_case_id) {
		this.pipeline_case_id = pipeline_case_id;
	}
	public String getJob_url() {
		return job_url;
	}
	public void setJob_url(String job_url) {
		this.job_url = job_url;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getDev_test_result() {
		return dev_test_result;
	}
	public void setDev_test_result(String dev_test_result) {
		this.dev_test_result = dev_test_result;
	}
	public String getDev_tester() {
		return dev_tester;
	}
	public void setDev_tester(String dev_tester) {
		this.dev_tester = dev_tester;
	}
	public String getQa_test_result() {
		return qa_test_result;
	}
	public void setQa_test_result(String qa_test_result) {
		this.qa_test_result = qa_test_result;
	}
	public String getQa_tester() {
		return qa_tester;
	}
	public void setQa_tester(String qa_tester) {
		this.qa_tester = qa_tester;
	}
	public String getUrgent_approver() {
		return urgent_approver;
	}
	public void setUrgent_approver(String urgent_approver) {
		this.urgent_approver = urgent_approver;
	}
	public String getUrgent_approve_result() {
		return urgent_approve_result;
	}
	public void setUrgent_approve_result(String urgent_approve_result) {
		this.urgent_approve_result = urgent_approve_result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getJob_log_url() {
		return job_log_url;
	}
	public void setJob_log_url(String job_log_url) {
		this.job_log_url = job_log_url;
	}
	public String getQa_test_time() {
		return qa_test_time;
	}
	public void setQa_test_time(String qa_test_time) {
		this.qa_test_time = qa_test_time;
	}
	public String getDev_test_time() {
		return dev_test_time;
	}
	public void setDev_test_time(String dev_test_time) {
		this.dev_test_time = dev_test_time;
	}
	public String getApprove_time() {
		return approve_time;
	}
	public void setApprove_time(String approve_time) {
		this.approve_time = approve_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
}
