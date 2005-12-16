/*
 * Created on 4/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author T�nia Pous�o
 * 
 */
public class InsertTutorShip implements IService {
    public Boolean verifyStudentOfThisDegree(IStudent student, DegreeType degreeType, String degreeCode)
            throws FenixServiceException, ExcepcaoPersistencia {
        boolean result = false;

        ISuportePersistente sp;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentStudentCurricularPlan persistentStudentCurricularPlan = sp
                .getIStudentCurricularPlanPersistente();

        IStudentCurricularPlan studentCurricularPlan = persistentStudentCurricularPlan
                .readActiveByStudentNumberAndDegreeType(student.getNumber(), degreeType);

        result = studentCurricularPlan.getDegreeCurricularPlan().getDegree().getSigla().equals(
                degreeCode);

        return Boolean.valueOf(result);
    }

    public Boolean verifyStudentAlreadyTutor(IStudent student, ITeacher teacher)
            throws FenixServiceException, ExcepcaoPersistencia {
        boolean result = false;
        ITutor tutor = null;

        ISuportePersistente sp;

        sp = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentTutor persistentTutor = sp.getIPersistentTutor();

        tutor = persistentTutor.readTeachersByStudent(student);

        if (tutor != null && !tutor.getTeacher().equals(teacher)) {
            result = true;
        }

        return new Boolean(result);
    }
}