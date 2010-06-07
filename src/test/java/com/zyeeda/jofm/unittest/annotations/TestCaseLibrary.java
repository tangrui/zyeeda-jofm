package com.zyeeda.jofm.unittest.annotations;

import com.zyeeda.jofm.annotations.OperationGroup;
import com.zyeeda.jofm.annotations.OperationGroups;
import com.zyeeda.jofm.annotations.Scope;
import com.zyeeda.jofm.annotations.Operation;

@Scope("test_case_library")
@OperationGroups({
	@OperationGroup(
		className = "com.zyeeda.ofm.handlers.ViewOperationHandler",
		operations = {
			@Operation(name = "show_create_view", methodName = "getCreateView"),
			@Operation(name = "show_update_view", methodName = "getUpdateView"),
			@Operation(name = "show_delete_view", methodName = "getDeleteView"),
			@Operation(name = "show_detail_view", methodName = "getDetailView"),
			@Operation(name = "show_list_view", methodName = "getListView")
		}
	),
	@OperationGroup(
		className = "com.zyeeda.ofm.handlers.ModelOperationHandler",
		operations = {
			@Operation(name = "get_create_model", methodName = "getCreateModel"),
			@Operation(name = "get_update_model", methodName = "getUpdateModel"),
			@Operation(name = "get_delete_model", methodName = "getDeleteModel"),
			@Operation(name = "get_list_model", methodName = "getListModel"),
			@Operation(name = "get_list_model", methodName = "getListModel"),
		}
	),
	@OperationGroup(
		className = "com.zyeeda.ofm.handlers.EntityOperationHandler",
		operations = {
			@Operation(name = "create", nextOperation="show_detail_view"),
			@Operation(name = "update", nextOperation="show_detail_view"),
			@Operation(name = "delete", nextOperation="show_list_view")
		}
	)
})
public class TestCaseLibrary {
}