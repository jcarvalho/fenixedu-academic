package net.sourceforge.fenixedu.presentationTier.Action.mobility.outbound;

import java.math.BigDecimal;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacy;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyPeriod;
import net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacySubmission;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/outboundMobilityCandidacy", module = "academicAdministration")
@Forwards({ @Forward(name = "prepare", path = "/mobility/outbound/OutboundMobilityCandidacy.jsp"),
        @Forward(name = "viewContest", path = "/mobility/outbound/viewContest.jsp"),
        @Forward(name = "manageCandidacies", path = "/mobility/outbound/manageCandidacies.jsp"),
        @Forward(name = "viewCandidate", path = "/mobility/outbound/viewCandidate.jsp"),
})
public class OutboundMobilityCandidacyDA extends FenixDispatchAction {

    public ActionForward prepare(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        if (outboundMobilityContextBean == null) {
            outboundMobilityContextBean = new OutboundMobilityContextBean();
        }
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward prepare(final ActionMapping mapping, final HttpServletRequest request,
            final OutboundMobilityContextBean outboundMobilityContextBean) {
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("prepare");
    }

    public ActionForward invalidadeAndPrepare(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward createNewOutboundMobilityCandidacyPeriod(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.createNewOutboundMobilityCandidacyPeriod();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward createNewOutboundMobilityCandidacyContest(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.createNewOutboundMobilityCandidacyContest();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward editCandidacyPeriod(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityCandidacyPeriod candidacyPeriod = getRenderedObject();
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setExecutionYear((ExecutionYear) candidacyPeriod.getExecutionInterval());
        outboundMobilityContextBean.setCandidacyPeriodsAsList(Collections.singletonList(candidacyPeriod));
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward addDegreeToGroup(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.addDegreeToGroup();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward removeDegreeFromGroup(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        final ExecutionDegree executionDegree = getDomainObject(request, "executionDegreeOid");
        outboundMobilityContextBean.setExecutionDegree(executionDegree);
        outboundMobilityContextBean.removeDegreeFromGroup();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward deleteContest(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        if (contest != null) {
            final OutboundMobilityCandidacyContestGroup mobilityGroup = contest.getOutboundMobilityCandidacyContestGroup();
            if (mobilityGroup.getOutboundMobilityCandidacyContestCount() == 1) {
                outboundMobilityContextBean.getMobilityGroups().remove(mobilityGroup);
            }
            contest.delete();
        }
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward viewContest(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        request.setAttribute("contest", contest);
        return mapping.findForward("viewContest");
    }

    public ActionForward viewContestForm(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityCandidacyContest contest = getDomainObject(request, "contestOid");
        request.setAttribute("contest", contest);
        return new ActionForward(viewContestPath(mapping, request, contest), true);
    }

    private String viewContestPath(final ActionMapping mapping, final HttpServletRequest request,
            final OutboundMobilityCandidacyContest contest) {
        final StringBuilder path = new StringBuilder();
        path.append(mapping.getModuleConfig().getPrefix());
        path.append("/outboundMobilityCandidacy.do?method=viewContest&contestOid=");
        path.append(contest.getExternalId());
        return constructRedirectPath(mapping, request, path);
    }

    private String constructRedirectPath(final ActionMapping mapping, final HttpServletRequest request, final StringBuilder path) {
        path.append('&');
        path.append(net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME);
        path.append('=');
        path.append(getFromRequest(request,
                net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter.CONTEXT_ATTRIBUTE_NAME));
        final String result = GenericChecksumRewriter.injectChecksumInUrl(request.getContextPath(), path.toString());
        return result.substring(mapping.getModuleConfig().getPrefix().length());
    }

    public ActionForward removeMobilityCoordinator(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();

        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");
        final Person person = getDomainObject(request, "personOid");
        mobilityGroup.removeMobilityCoordinatorService(person);

        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward addMobilityCoordinator(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        outboundMobilityContextBean.addMobilityCoordinator();
        RenderUtils.invalidateViewState();
        return prepare(mapping, request, outboundMobilityContextBean);
    }

    public ActionForward manageCandidacies(final ActionMapping mapping, final ActionForm actionForm,
            final HttpServletRequest request, final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = getRenderedObject();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("manageCandidacies");
    }

    public ActionForward editGrade(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityCandidacySubmission submission = getDomainObject(request, "candidacySubmissionOid");
        final OutboundMobilityCandidacyContestGroup mobilityGroup = getDomainObject(request, "mobilityGroupOid");
        final String grade = (String) getFromRequest(request, "grade");
        submission.setGrade(mobilityGroup, new BigDecimal(grade));
        return null;
    }

    public ActionForward viewCandidate(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        final Person person = getDomainObject(request, "personOid");
        if (person != null) {
            outboundMobilityContextBean.setPerson(person);
        }
        final ExecutionYear executionYear = getDomainObject(request, "executionYearOid");
        if (executionYear != null) {
            outboundMobilityContextBean.setExecutionYear(executionYear);
        }
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("viewCandidate");
    }

    public ActionForward selectCandite(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        candidacy.select();
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setPerson(candidacy.getOutboundMobilityCandidacySubmission().getRegistration().getPerson());
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("viewCandidate");
    }

    public ActionForward unselectCandite(final ActionMapping mapping, final ActionForm actionForm, final HttpServletRequest request,
            final HttpServletResponse response) {
        final OutboundMobilityCandidacy candidacy = getDomainObject(request, "candidacyOid");
        candidacy.unselect();
        final OutboundMobilityContextBean outboundMobilityContextBean = new OutboundMobilityContextBean();
        outboundMobilityContextBean.setPerson(candidacy.getOutboundMobilityCandidacySubmission().getRegistration().getPerson());
        RenderUtils.invalidateViewState();
        request.setAttribute("outboundMobilityContextBean", outboundMobilityContextBean);
        return mapping.findForward("viewCandidate");
    }
    
}
