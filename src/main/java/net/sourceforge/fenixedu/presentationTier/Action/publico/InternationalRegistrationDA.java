package net.sourceforge.fenixedu.presentationTier.Action.publico;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.person.InitializePassword;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PublicCandidacyHashCode;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "external", path = "/internationalRegistration", scope = "request", parameter = "method", validate = true,
        attribute = "internationalRegistrationForm", formBean = "internationalRegistrationForm",
        formBeanClass = InternationalRegistrationForm.class)
@Forwards({ @Forward(name = "international-registration", path = "international-registration"),
        @Forward(name = "success", path = "success") })
public class InternationalRegistrationDA extends FenixDispatchAction {

    public ActionForward showInternationalRegistration(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        Person person = readPersonByCandidacyHashCode(request.getParameter("hash"));
        if (person != null) {
            request.setAttribute("person", person);
            return mapping.findForward("international-registration");
        } else {
            return setError(request, mapping, "internationalRegistration.error.invalidLink", null, null);
        }

    }

    public ActionForward updateUserPassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        if (!(form instanceof InternationalRegistrationForm)) {
            return setError(request, mapping, "internationalRegistration.error.invalidLink", "international-registration", null);
        }

        InternationalRegistrationForm registrationForm = (InternationalRegistrationForm) form;
        Person person = readPersonByCandidacyHashCode(registrationForm.getHashCode());

        request.setAttribute("person", person);

        if (person == null) {
            return setError(request, mapping, "internationalRegistration.error.invalidLink", "international-registration", null);
        }

        if (!StringUtils.equals(registrationForm.getPassword(), registrationForm.getRetypedPassword())) {
            return setError(request, mapping, "internationalRegistration.error.passwordsDontMatch", "international-registration",
                    null);
        }

        if (!isValidPassword(registrationForm.getPassword())) {
            return setError(request, mapping, "internationalRegistration.error.invalidPassword", "international-registration",
                    null);
        }

        try {
            InitializePassword.run(person.getUser(), registrationForm.getPassword());
        } catch (Exception e) {
            return setError(request, mapping, "internationalRegistration.error.registering", "international-registration", e);
        }
        return mapping.findForward("success");
    }

    private boolean isValidPassword(String password) {
        final String regEx = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,20})";
        return Pattern.matches(regEx, password);
    }

    private Person readPersonByCandidacyHashCode(String hashCode) {
        if (!StringUtils.isEmpty(hashCode)) {
            PublicCandidacyHashCode publicCandidacyHashCode = PublicCandidacyHashCode.getPublicCandidacyCodeByHash(hashCode);
            return publicCandidacyHashCode != null ? Person.readPersonByEmailAddress(publicCandidacyHashCode.getEmail()) : null;
        }
        return null;
    }

}
