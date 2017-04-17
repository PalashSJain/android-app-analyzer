package testinfo;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palash on 4/17/2017.
 */
public class TestInfo {
    private final InputStream file;
    private String name;
    private List<MethodInfo> methods;

    public TestInfo(InputStream file) {
        this.file = file;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<MethodInfo> getMethods() {
        return methods;
    }

    public void scan() throws FileNotFoundException {
        CompilationUnit cu = JavaParser.parse(file);
        methods = new ArrayList<>();
        new ClassVisitor().visit(cu, null);
    }

    private class ClassVisitor extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, Void arg) {
            name = n.getNameAsString();
            super.visit(n, arg);
        }

        @Override
        public void visit(MethodDeclaration n, Void arg) {
            if (n.getNameAsString().contains("test") || n.getAnnotationByName("Test").isPresent()) {
                MethodInfo method = new MethodInfo();
                method.setName(n.getNameAsString());
                method.setLinesOfCode(n.getEnd().get().line - n.getBegin().get().line);
                methods.add(method);
            }
            super.visit(n, arg);
        }
    }
}
