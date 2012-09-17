package net.sourceforge.fenixedu.presentationTier.Action.credits.departmentAdmOffice;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Department;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.credits.util.TeacherCreditsBean;
import net.sourceforge.fenixedu.domain.organizationalStructure.DepartmentUnit;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.presentationTier.Action.credits.ViewTeacherCreditsDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "departmentAdmOffice", path = "/credits", scope = "request", parameter = "method")
@Forwards(value = { @Forward(name = "selectTeacher", path = "/credits/selectTeacher.jsp"),
	@Forward(name = "showTeacherCredits", path = "/credits/showTeacherCredits.jsp"),
	@Forward(name = "showPastTeacherCredits", path = "/credits/showPastTeacherCredits.jsp"),
	@Forward(name = "showAnnualTeacherCredits", path = "/credits/showAnnualTeacherCredits.jsp") })
public class DepartmentAdmOfficeViewTeacherCreditsDA extends ViewTeacherCreditsDA {
    @Override
    public ActionForward viewAnnualTeachingCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	return viewAnnualTeachingCredits(mapping, form, request, response, RoleType.DEPARTMENT_ADMINISTRATIVE_OFFICE);
    }

    @Override
    public ActionForward showTeacherCredits(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws NumberFormatException, FenixServiceException, Exception {
	TeacherCreditsBean teacherBean = getRenderedObject();

	if (!isTeacherOfManageableDepartments(teacherBean.getTeacher())) {
	    addActionMessage("error", request, "message.teacher.not-found-or-not-belong-to-department");
	    return prepareTeacherSearch(mapping, form, request, response);
	}

	teacherBean.prepareAnnualTeachingCredits();
	request.setAttribute("teacherBean", teacherBean);
	return mapping.findForward("showTeacherCredits");
    }

    private boolean isTeacherOfManageableDepartments(Teacher teacher) {
	IUserView userView = UserView.getUser();
	ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	List<Department> manageableDepartments = userView.getPerson().getManageableDepartmentCredits();
	List<Unit> workingPlacesByPeriod = teacher.getWorkingPlacesByPeriod(executionSemester.getBeginDateYearMonthDay(),
		executionSemester.getEndDateYearMonthDay());
	for (Unit unit : workingPlacesByPeriod) {
	    DepartmentUnit departmentUnit = unit.getDepartmentUnit();
	    Department teacherDepartment = departmentUnit != null ? departmentUnit.getDepartment() : null;
	    if (teacherDepartment != null && manageableDepartments.contains(teacherDepartment)) {
		return true;
	    }
	}
	return false;
    }

}