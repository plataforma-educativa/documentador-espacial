package espacial.documentador;

import com.sun.javadoc.*;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class JavaDoc {

    private final RootDoc root;

    private boolean constructorIncluded;
    private boolean methodIncluded;

    private JavaDoc(RootDoc rootDoc) {

        root = rootDoc;
    }

    private String priorityOf(ClassDoc clazz) {

        Tag[] tags = clazz.tags("prioridad");

        return tags.length > 0 ? tags[0].text() : "";
    }

    private int compareClass(ClassDoc class1, ClassDoc class2) {

        return priorityOf(class1).compareTo(priorityOf(class2));
    }

    private Set<ClassDoc> extractClasses() {

        Set<ClassDoc> classes = new TreeSet<>(this::compareClass);
        for (ClassDoc clazz : root.classes()) {
            if (mustInclude(clazz)) {
                classes.add(clazz);
            }
        }
        return classes;
    }

    private void execute() {

        for (ClassDoc clazz : extractClasses()) {

            constructorIncluded = false;
            methodIncluded = false;

            if (mustInclude(clazz)) {

                includeType(clazz);

                includeDescription(clazz);

                if (isEnum(clazz)) {

                    includeEnumValues(clazz);

                } else {

                    includeClass(clazz);
                }
            }
        }
    }

    private void includeType(ClassDoc clazz) {

        include(title1("Tipo"));

        include(code(typeOf(clazz)));
    }

    private String title1(String value) {

        return "<h3>" + value + "</h3>";
    }

    private void includeDescription(ClassDoc clazz) {

        include(title2("Descripción"));

        include(normal(descriptionOf(clazz)));
    }

    private String title2(String value) {

        return "<h4>" + value +"</h4>";
    }

    private String normal(String value) {

        return "<p>" + value + "</p>";
    }

    private void includeEnumValues(ClassDoc clazz) {

        include(title2("Valores"));
        include("<div class=\"row\"><div class=\"six columns\">");
        for (FieldDoc field : clazz.fields()) {

            if (mustInclude(field)) {

                includeEnumValue(field);
            }
        }
        include("</div><div class=\"six columns\">&nbsp;</div></div>");
    }

    private String typeOf(ClassDoc clazz) {

        return clazz.simpleTypeName();
    }

    private String code(String value, Object... params) {

        return "<pre><code>" + String.format(value, params) + "</code></pre>";
    }

    private String doc(String value, Object... params) {

        return "<li>" + String.format(value, params) + "</li>";
    }

    private void includeEnumValue(FieldDoc field) {

        include(code(field.name()));
    }

    private boolean mustInclude(FieldDoc field) {
        
        return field.isStatic() && field.isPublic();
    }

    private boolean isEnum(ClassDoc clazz) {

        /* por alguna razón el método isEnum siempre devuelve false*/
        return Enum.class.getName().equals(clazz.superclass().qualifiedName());
    }

    private void includeClass(ClassDoc clazz) {

        for (ConstructorDoc constructor : clazz.constructors()) {

            includeConstructor(constructor);
        }
        for (MethodDoc method : clazz.methods()) {

            includeMethod(method);
        }
    }

    private void includeConstructor(ConstructorDoc constructor) {

        if (mustInclude(constructor)) {

            if (!constructorIncluded) {
                constructorIncluded = !constructorIncluded;
                include(title2("Constructores"));
            }

            include("<div class=\"row\"><div class=\"one column\">&nbsp;</div><div class=\"eleven columns\">");
            include(code("%s(%s)", nameOf(constructor), paramsOf(constructor)));
            include("</div></div>");
            includeContract(constructor);
        }
    }

    private boolean mustInclude(ConstructorDoc constructor) {

        return constructor.isPublic();
    }

    private String nameOf(ConstructorDoc constructor) {

        return constructor.name();
    }

    private String paramsOf(ConstructorDoc constructor) {

        return definitionOf(constructor.parameters());
    }

    private void includeMethod(MethodDoc method) {

        if (mustInclude(method)) {

            if (! methodIncluded) {
                methodIncluded = !methodIncluded;
                include(title2("Métodos"));
            }

            include("<div class=\"row\"><div class=\"one column\">&nbsp;</div><div class=\"eleven columns\">");
            include(code("%s %s(%s)", returnOf(method), nameOf(method), paramsOf(method)));
            include("</div></div>");
            includeContract(method);
        }
    }

    private boolean mustInclude(MethodDoc method) {

        return method.isPublic() && ! method.isStatic() && !"toString".equals(method.name());
    }

    private boolean mustInclude(ClassDoc clazz) {

        return clazz.isPublic() && "".equals(clazz.containingPackage().name());
    }

    private void includeContract(ExecutableMemberDoc executable) {

        include("<div class=\"row\"><div class=\"two columns\">&nbsp;</div><div class=\"ten columns\">");
        include("<ul>");
        includePre(executable);
        includeParams(executable);
        includePost(executable);
        includeReturn(executable);
        include("</ul>");
        include("</div></div>");
    }

    private void includePre(ExecutableMemberDoc executable) {

        for (Tag tag: executable.tags("pre")) {
            include(doc("<strong>pre :</strong> %s", valueOf(tag)));
        }
    }

    private void includeParams(ExecutableMemberDoc executable) {

        for (ParamTag paramTag: executable.paramTags()) {
            include(doc("parametro %s : %s", paramTag.parameterName(), paramTag.parameterComment()));
        }
    }

    private void includePost(ExecutableMemberDoc executable) {

        for (Tag tag: executable.tags("post")) {
            include(doc("<strong>post:</strong> %s", valueOf(tag)));
        }
    }

    private void includeReturn(ExecutableMemberDoc executable) {

        for (Tag tag: executable.tags("return")) {
            include(doc("retorno : %s", valueOf(tag)));
        }
    }

    private String descriptionOf(ClassDoc clazz) {

        return unwrap(clazz.commentText());
    }

    private String returnOf(MethodDoc method) {

        return method.returnType().simpleTypeName();
    }

    private String nameOf(MethodDoc method) {

        return method.name();
    }

    private String paramsOf(MethodDoc method) {

        return definitionOf(method.parameters());
    }

    private String definitionOf(Parameter[] parameters) {

        return Arrays.stream(parameters)
                .map(parameter -> parameter.type().simpleTypeName() + " " + parameter.name())
                .collect(Collectors.joining(", "));
    }

    private String valueOf(Tag tag) {

        return unwrap(tag.text());
    }

    private String unwrap(String value) {

        return value.replace('\n', ' ').replaceAll(" +", " ");
    }

    private void include(String texto, Object... parametros) {

        System.out.printf(texto + "%n", parametros);
    }

    public static boolean start(RootDoc root) {

        new JavaDoc(root).execute();

        return true;
    }
}
