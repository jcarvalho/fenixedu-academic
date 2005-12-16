package net.sourceforge.fenixedu.applicationTier.Servico.commons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseScopeWithBranchAndSemesterAndYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourseWithInfoDegree;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.ICurricularCourseScope;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 */

public class ReadCurricularCourseScopesByExecutionCourseID implements IService {

    public List run(Integer executionCourseID) throws FenixServiceException, ExcepcaoPersistencia {

        List infoCurricularCourses = new ArrayList();

        ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

        // Read The ExecutionCourse

        IExecutionCourse executionCourse = (IExecutionCourse) sp.getIPersistentExecutionCourse()
                .readByOID(ExecutionCourse.class, executionCourseID);

        // For all associated Curricular Courses read the Scopes

        infoCurricularCourses = new ArrayList();
        Iterator iterator = executionCourse.getAssociatedCurricularCourses().iterator();
        while (iterator.hasNext()) {
            ICurricularCourse curricularCourse = (ICurricularCourse) iterator.next();

            IExecutionPeriod executionPeriod = executionCourse.getExecutionPeriod();
            List curricularCourseScopes = sp.getIPersistentCurricularCourseScope()
                    .readCurricularCourseScopesByCurricularCourseInExecutionPeriod(
                            curricularCourse.getIdInternal(), executionPeriod.getBeginDate(),
                            executionPeriod.getEndDate());

            InfoCurricularCourse infoCurricularCourse = InfoCurricularCourseWithInfoDegree
                    .newInfoFromDomain(curricularCourse);
            infoCurricularCourse.setInfoScopes(new ArrayList());

            Iterator scopeIterator = curricularCourseScopes.iterator();
            while (scopeIterator.hasNext()) {

                infoCurricularCourse.getInfoScopes().add(
                        InfoCurricularCourseScopeWithBranchAndSemesterAndYear
                                .newInfoFromDomain((ICurricularCourseScope) scopeIterator.next()));
            }
            infoCurricularCourses.add(infoCurricularCourse);
        }

        return infoCurricularCourses;
    }
}