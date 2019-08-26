package espacial.documentador;

import com.sun.javadoc.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class JavaDoc {

    private final RootDoc root;

    private final Documentacion documentacion;

    private JavaDoc(RootDoc rootDoc) {

        documentacion = new Documentacion("./contratos.html");
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

        documentacion.escribirEncabezado();

        for (ClassDoc clazz : extractClasses()) {

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

        documentacion.escribirPie();
        documentacion.terminar();
    }

    private void includeType(ClassDoc clazz) {

        documentacion.escribirTipo(typeOf(clazz));
    }

    private void includeDescription(ClassDoc clazz) {

        documentacion.escribirDescripcion(descriptionOf(clazz));
    }

    private void includeEnumValues(ClassDoc clazz) {

        List<String> values = new LinkedList<>();

        for (FieldDoc field : clazz.fields()) {

            if (mustInclude(field)) {
                values.add(field.name());
            }
        }

        documentacion.escribirValoresEnumerados(values);
    }

    private String typeOf(ClassDoc clazz) {

        return clazz.simpleTypeName();
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

            documentacion.escribirConstructor(format("%s(%s)", nameOf(constructor), paramsOf(constructor)));

            includeContract(constructor);
        }
    }

    private String format(String format, Object... args) {

        return String.format(format, args);
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

            documentacion.escribirMetodo(format("%s %s(%s)", returnOf(method), nameOf(method), paramsOf(method)));

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

        includePre(executable);
        includeParams(executable);
        includePost(executable);
        includeReturn(executable);
    }

    private void includePre(ExecutableMemberDoc executable) {

        for (Tag tag: executable.tags("pre")) {

            documentacion.escribirPre(valueOf(tag));
        }
    }

    private void includeParams(ExecutableMemberDoc executable) {

        for (ParamTag paramTag: executable.paramTags()) {

            documentacion.escribirParametro(paramTag.parameterName(), paramTag.parameterComment());
        }
    }

    private void includePost(ExecutableMemberDoc executable) {

        for (Tag tag: executable.tags("post")) {

            documentacion.escribirPost(valueOf(tag));
        }
    }

    private void includeReturn(ExecutableMemberDoc executable) {

        for (Tag tag: executable.tags("return")) {

            documentacion.escribirRetorno(valueOf(tag));
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

    public static boolean start(RootDoc root) {

        new JavaDoc(root).execute();

        return true;
    }
}