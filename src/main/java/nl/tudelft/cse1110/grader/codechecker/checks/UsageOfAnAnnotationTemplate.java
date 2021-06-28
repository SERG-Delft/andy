package nl.tudelft.cse1110.grader.codechecker.checks;

import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;

import java.util.List;

public abstract class UsageOfAnAnnotationTemplate extends Check {
    protected final int minimumNumberOfUsage;
    private final Comparison comparison;
    private int numberOfTestsInCU = 0;

    public UsageOfAnAnnotationTemplate(List<String> params) {
        assert params!=null;
        assert params.size() == 2;

        comparison = ComparisonFactory.build(params.get(0));
        this.minimumNumberOfUsage = Integer.parseInt(params.get(1));
        assert minimumNumberOfUsage >= 0;
    }

    @Override
    public boolean visit(MarkerAnnotation node) {
        count(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(NormalAnnotation node) {
        count(node.getTypeName());
        return true;
    }

    @Override
    public boolean visit(SingleMemberAnnotation node) {
        count(node.getTypeName());
        return true;
    }

    private void count(Name name) {
        if (annotationName().equals(name.getFullyQualifiedName()))
            numberOfTestsInCU++;
    }

    protected abstract String annotationName();

    @Override
    public boolean result() {
        return comparison.compare(numberOfTestsInCU, minimumNumberOfUsage);
    }
}
