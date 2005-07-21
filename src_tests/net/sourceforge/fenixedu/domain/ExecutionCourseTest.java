/*
 * Created on Jun 29, 2005
 *  by jdnf and mrsp
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Date;

public class ExecutionCourseTest extends DomainTestBase {

    private IExecutionCourse executionCourse;
	
	private IExecutionCourse executionCourseToReadFrom = null;
	private IStudent thisStudent = null;
	private IAttends attendsForThisStudent = null;

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);

        executionCourse.setNome("name");
        executionCourse.setSigla("acronym");
        executionCourse.setTheoreticalHours(4.0);
        executionCourse.setTheoPratHours(0.0);
        executionCourse.setPraticalHours(2.0);
        executionCourse.setLabHours(0.0);
        executionCourse.setComment("comment");
		
		setUpForGetAttendsByStudentCase();
    }

    private void setUpForGetAttendsByStudentCase() {
		executionCourseToReadFrom = new ExecutionCourse();
		thisStudent = new Student();
		attendsForThisStudent = new Attends();
		
		attendsForThisStudent.setAluno(thisStudent);
		executionCourseToReadFrom.addAttends(attendsForThisStudent);
		
		IAttends otherAttends1 = new Attends();
		otherAttends1.setAluno(new Student());
		executionCourseToReadFrom.addAttends(otherAttends1);
		
		IAttends otherAttends2 = new Attends();
		otherAttends2.setAluno(new Student());
		executionCourseToReadFrom.addAttends(otherAttends2);
	}

	public void testEdit() {
        try {
            executionCourse.edit(null, null, 0.0, 0.0, 0.0, 0.0, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfExecutionCourseAttributesAreCorrect("name", "acronym", 4.0, 0.0, 2.0, 0.0, "comment");
        }

        try {
            executionCourse.edit(null, null, 0.0, 2.0, 0.0, 0.0, "newComment");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfExecutionCourseAttributesAreCorrect("name", "acronym", 4.0, 0.0, 2.0, 0.0, "comment");
        }

        try {
            executionCourse.edit("newName", null, 0.0, 0.0, 4.0, 0.0, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            checkIfExecutionCourseAttributesAreCorrect("name", "acronym", 4.0, 0.0, 2.0, 0.0, "comment");
        }

        executionCourse.edit("newName", "newAcronym", 2.0, 1.0, 2.0, 1.0, "newComment");
        checkIfExecutionCourseAttributesAreCorrect("newName", "newAcronym", 2.0, 1.0, 2.0, 1.0,
                "newComment");
    }
    
    public void testCreateSite() {
        executionCourse.setSite(null);

        assertNull("Expected Null Site in ExecutionCourse!", executionCourse.getSite());
        executionCourse.createSite();
        assertNotNull("Expected Not Null Site in ExecutionCourse!", executionCourse.getSite());
        assertTrue("Expected the Same Execution Course in created Site!", executionCourse.getSite()
                .getExecutionCourse().equals(executionCourse));
        assertEquals("Size unexpected in Site Announcements!", 0, executionCourse.getSite()
                .getAssociatedAnnouncementsCount());
        assertEquals("Size unexpected in Site Sections!", 0, executionCourse.getSite()
                .getAssociatedSectionsCount());
    }

    public void testCreateEvaluationMethod() {
        executionCourse.setEvaluationMethod(null);

        assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                .getEvaluationMethod());

        try {
            executionCourse.createEvaluationMethod(null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        try {
            executionCourse.createEvaluationMethod(null, "evaluationElementsEng");
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        try {
            executionCourse.createEvaluationMethod("evaluationElements", null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertNull("Expected Null Evaluation Method in ExecutionCourse!", executionCourse
                    .getEvaluationMethod());
        }

        executionCourse.createEvaluationMethod("evaluationElements", "evaluationElementsEng");
        assertNotNull("Expected Not Null EvaluationMethod in ExecutionCourse!", executionCourse
                .getEvaluationMethod());
        assertEquals("Different EvaluationElements in EvaluationMethod!", "evaluationElements",
                executionCourse.getEvaluationMethod().getEvaluationElements());
        assertEquals("Different EvaluationElementsEng in EvaluationMethod!", "evaluationElementsEng",
                executionCourse.getEvaluationMethod().getEvaluationElementsEn());
        assertTrue("Different ExecutionCourse in EvaluationMethod!", executionCourse
                .getEvaluationMethod().getExecutionCourse().equals(executionCourse));

    }

    public void testCreateBibliographicReference() {
        assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                .getAssociatedBibliographicReferencesCount());
        try {
            executionCourse.createBibliographicReference(null, null, null, null, null);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        try {
            executionCourse.createBibliographicReference("title", null, "reference", null, true);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        try {
            executionCourse.createBibliographicReference(null, "authors", null, "year", true);
            fail("Expected NullPointerException!");
        } catch (NullPointerException e) {
            assertEquals("Size unexpected for AssociatedBibliographicReference!", 0, executionCourse
                    .getAssociatedBibliographicReferencesCount());
        }

        executionCourse.createBibliographicReference("title", "authors", "reference", "year", true);
        assertEquals("Size unexpected for AssociatedBibliographicReference!", 1, executionCourse
                .getAssociatedBibliographicReferencesCount());
        assertEquals("Different Title in BibliographicReference!", "title", executionCourse
                .getAssociatedBibliographicReferences(0).getTitle());
        assertEquals("Different Authors in BibliographicReference!", "authors", executionCourse
                .getAssociatedBibliographicReferences(0).getAuthors());
        assertEquals("Different Reference in BibliographicReference!", "reference", executionCourse
                .getAssociatedBibliographicReferences(0).getReference());
        assertEquals("Different Year in BibliographicReference!", "year", executionCourse
                .getAssociatedBibliographicReferences(0).getYear());
        assertTrue("Different Optional in BibliographicReference!", executionCourse
                .getAssociatedBibliographicReferences(0).getOptional());
        assertEquals("Different ExecutionCourse in BibliographicReference!", executionCourse,
                executionCourse.getAssociatedBibliographicReferences(0).getExecutionCourse());
    }

    public void testCreateCourseReport() {
        executionCourse.setCourseReport(null);
        assertNull("Expected Null CourseReport in ExecutionCourse!", executionCourse.getCourseReport());

        try {
            executionCourse.createCourseReport(null);
            fail("Expected Null Pointer Exception");
        } catch (NullPointerException e) {
            assertNull("Expected Null CourseReport in ExecutionCourse!", executionCourse
                    .getCourseReport());
        }

        final Date dateBeforeCreation = Calendar.getInstance().getTime();
        sleep(1000);
        executionCourse.createCourseReport("report");
        sleep(1000);
        final Date dateAfterCreation = Calendar.getInstance().getTime();

        assertEquals("Different Report in CourseReport!", "report", executionCourse.getCourseReport()
                .getReport());
        assertTrue("Expected LastModificationDate After an initial timestamp!", executionCourse
                .getCourseReport().getLastModificationDate().after(dateBeforeCreation));
        assertTrue("Expected LastModificationDate Before an initial timestamp!", executionCourse
                .getCourseReport().getLastModificationDate().before(dateAfterCreation));
        assertEquals("Different ExecutionCourse in CourseReport!", executionCourse.getCourseReport()
                .getExecutionCourse(), executionCourse);
    }
    
    private void checkIfExecutionCourseAttributesAreCorrect(final String name, final String acronym,
            final double theoreticalHours, final double theoreticalPraticalHours,
            final double praticalHours, final double laboratoryHours, final String comment) {

        assertEquals("Different ExecutionCourse Name!", name, executionCourse.getNome());
        assertEquals("Different ExecutionCourse Acronym!", acronym, executionCourse.getSigla());
        assertEquals("Different ExecutionCourse Theoretical Hours!", theoreticalHours, executionCourse
                .getTheoreticalHours());
        assertEquals("Different ExecutionCourse TheoreticalPratical Hours!", theoreticalPraticalHours,
                executionCourse.getTheoPratHours());
        assertEquals("Different ExecutionCourse Pratical Hours!", praticalHours, executionCourse
                .getPraticalHours());
        assertEquals("Different ExecutionCourse Laboratory Hours!", laboratoryHours, executionCourse
                .getLabHours());
        assertEquals("Different ExecutionCourse Comment!", comment, executionCourse.getComment());
    }
	
	
	public void testGetAttendsByStudent() {
		IAttends attends = executionCourseToReadFrom.getAttendsByStudent(thisStudent);
		
		assertEquals(attends,attendsForThisStudent);
	}
}
