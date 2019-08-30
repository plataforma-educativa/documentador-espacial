package espacial.documentador;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Documentacion {

    private final PrintStream archivo;

    private AtomicBoolean escribioUnConstructor = new AtomicBoolean();
    private AtomicBoolean escribioUnMetodo = new AtomicBoolean();

    public Documentacion(String ruta) {

        archivo = abrirArchivo(ruta);
    }

    private void contenidoCon(String formato, Object... parametros) {

        archivo.printf(formato, parametros);
    }

    private void lineaCon(String formato, Object... parametro) {

        contenidoCon(formato + "%n", parametro);
    }

    private void lineaCon(String formato) {

        archivo.println(formato);
    }


    private void parrafoCon(String valor) {

        lineaCon("<p>%s</p>", valor);
    }

    private void tituloCon(String valor) {

        lineaCon("<h1>%s</h1>", valor);
    }

    private void subtituloCon(String valor) {

        lineaCon("<h3>%s</h3>", valor);
    }

    private void seccionCon(String valor) {

        lineaCon("<h4>%s</h4>", valor);
    }

    private void codigoCon(String valor) {

        lineaCon("<pre><code>%s</code></pre>", valor);
    }

    private void operacionCon(String valor) {

        inicioFila();
        lineaCon("<div class=\"one column\">&nbsp;</div>");
        lineaCon("<div class=\"eleven columns\">");
        codigoCon(valor);
        lineaCon("</div>");
        finFila();
    }

    private void condicionCon(String tipo, String texto) {

        inicioFila();
        lineaCon("<div class=\"two columns\">&nbsp;</div>");
        lineaCon("<div class=\"one column\"><strong>%s</strong></div>", tipo);
        lineaCon("<div class=\"nine columns\">%s</div>", texto);
        finFila();
    }

    private void separador() {

        lineaCon("<hr></hr>");
    }

    private void inicioPanel() {

        lineaCon("<div class=\"container\">");
    }

    private void finPanel() {

        lineaCon("</div>");
    }

    private void inicioFila() {

        lineaCon("<div class=\"row\">");
    }

    private void finFila() {

        finPanel();
    }

    public void escribirEncabezado() {

        lineaCon("<!DOCTYPE html>");
        lineaCon("<html lang=\"es\">");
        lineaCon("<head>");
        lineaCon("<meta content=\"text/html; charset=UTF-8\" http-equiv=\"content-type\"/>");
        lineaCon("<style type=\"text/css\">");

        lineaCon("/*! normalize.css v3.0.2 | MIT License | git.io/normalize */html{font-family:sans-serif;-ms-text-size-adjust:100%;-webkit-text-size-adjust:100%}body{margin:0}article,aside,details,figcaption,figure,footer,header,hgroup,main,menu,nav,section,summary{display:block}audio,canvas,progress,video{display:inline-block;vertical-align:baseline}audio:not([controls]){display:none;height:0}[hidden],template{display:none}a{background-color:transparent}a:active,a:hover{outline:0}abbr[title]{border-bottom:1px dotted}b,strong{font-weight:700}dfn{font-style:italic}h1{font-size:2em;margin:.67em 0}mark{background:#ff0;color:#000}small{font-size:80%}sub,sup{font-size:75%;line-height:0;position:relative;vertical-align:baseline}sup{top:-.5em}sub{bottom:-.25em}img{border:0}svg:not(:root){overflow:hidden}figure{margin:1em 40px}hr{-moz-box-sizing:content-box;box-sizing:content-box;height:0}pre{overflow:auto}code,kbd,pre,samp{font-family:monospace,monospace;font-size:1em}button,input,optgroup,select,textarea{color:inherit;font:inherit;margin:0}button{overflow:visible}button,select{text-transform:none}button,html input[type=button],input[type=reset],input[type=submit]{-webkit-appearance:button;cursor:pointer}button[disabled],html input[disabled]{cursor:default}button::-moz-focus-inner,input::-moz-focus-inner{border:0;padding:0}input{line-height:normal}input[type=checkbox],input[type=radio]{box-sizing:border-box;padding:0}input[type=number]::-webkit-inner-spin-button,input[type=number]::-webkit-outer-spin-button{height:auto}input[type=search]{-webkit-appearance:textfield;-moz-box-sizing:content-box;-webkit-box-sizing:content-box;box-sizing:content-box}input[type=search]::-webkit-search-cancel-button,input[type=search]::-webkit-search-decoration{-webkit-appearance:none}fieldset{border:1px solid silver;margin:0 2px;padding:.35em .625em .75em}legend{border:0;padding:0}textarea{overflow:auto}optgroup{font-weight:700}table{border-collapse:collapse;border-spacing:0}td,th{padding:0}.container{position:relative;width:100%;max-width:960px;margin:0 auto;padding:0 20px;box-sizing:border-box}.column,.columns{width:100%;float:left;box-sizing:border-box}@media (min-width:400px){.container{width:85%;padding:0}}@media (min-width:550px){.container{width:80%}.column,.columns{margin-left:4%}.column:first-child,.columns:first-child{margin-left:0}.one.column,.one.columns{width:4.66666666667%}.two.columns{width:13.3333333333%}.three.columns{width:22%}.four.columns{width:30.6666666667%}.five.columns{width:39.3333333333%}.six.columns{width:48%}.seven.columns{width:56.6666666667%}.eight.columns{width:65.3333333333%}.nine.columns{width:74%}.ten.columns{width:82.6666666667%}.eleven.columns{width:91.3333333333%}.twelve.columns{width:100%;margin-left:0}.one-third.column{width:30.6666666667%}.two-thirds.column{width:65.3333333333%}.one-half.column{width:48%}.offset-by-one.column,.offset-by-one.columns{margin-left:8.66666666667%}.offset-by-two.column,.offset-by-two.columns{margin-left:17.3333333333%}.offset-by-three.column,.offset-by-three.columns{margin-left:26%}.offset-by-four.column,.offset-by-four.columns{margin-left:34.6666666667%}.offset-by-five.column,.offset-by-five.columns{margin-left:43.3333333333%}.offset-by-six.column,.offset-by-six.columns{margin-left:52%}.offset-by-seven.column,.offset-by-seven.columns{margin-left:60.6666666667%}.offset-by-eight.column,.offset-by-eight.columns{margin-left:69.3333333333%}.offset-by-nine.column,.offset-by-nine.columns{margin-left:78%}.offset-by-ten.column,.offset-by-ten.columns{margin-left:86.6666666667%}.offset-by-eleven.column,.offset-by-eleven.columns{margin-left:95.3333333333%}.offset-by-one-third.column,.offset-by-one-third.columns{margin-left:34.6666666667%}.offset-by-two-thirds.column,.offset-by-two-thirds.columns{margin-left:69.3333333333%}.offset-by-one-half.column,.offset-by-one-half.columns{margin-left:52%}}html{font-size:62.5%}body{font-size:1.5em;line-height:1.6;font-weight:400;font-family:Raleway,HelveticaNeue,\"Helvetica Neue\",Helvetica,Arial,sans-serif;color:#222}h1,h2,h3,h4,h5,h6{margin-top:0;margin-bottom:2rem;font-weight:300}h1{font-size:4rem;line-height:1.2;letter-spacing:-.1rem}h2{font-size:3.6rem;line-height:1.25;letter-spacing:-.1rem}h3{font-size:3rem;line-height:1.3;letter-spacing:-.1rem}h4{font-size:2.4rem;line-height:1.35;letter-spacing:-.08rem}h5{font-size:1.8rem;line-height:1.5;letter-spacing:-.05rem}h6{font-size:1.5rem;line-height:1.6;letter-spacing:0}@media (min-width:550px){h1{font-size:5rem}h2{font-size:4.2rem}h3{font-size:3.6rem}h4{font-size:3rem}h5{font-size:2.4rem}h6{font-size:1.5rem}}p{margin-top:0}a{color:#1eaedb}a:hover{color:#0fa0ce}.button,button,input[type=button],input[type=reset],input[type=submit]{display:inline-block;height:38px;padding:0 30px;color:#555;text-align:center;font-size:11px;font-weight:600;line-height:38px;letter-spacing:.1rem;text-transform:uppercase;text-decoration:none;white-space:nowrap;background-color:transparent;border-radius:4px;border:1px solid #bbb;cursor:pointer;box-sizing:border-box}.button:focus,.button:hover,button:focus,button:hover,input[type=button]:focus,input[type=button]:hover,input[type=reset]:focus,input[type=reset]:hover,input[type=submit]:focus,input[type=submit]:hover{color:#333;border-color:#888;outline:0}.button.button-primary,button.button-primary,input[type=button].button-primary,input[type=reset].button-primary,input[type=submit].button-primary{color:#fff;background-color:#33c3f0;border-color:#33c3f0}.button.button-primary:focus,.button.button-primary:hover,button.button-primary:focus,button.button-primary:hover,input[type=button].button-primary:focus,input[type=button].button-primary:hover,input[type=reset].button-primary:focus,input[type=reset].button-primary:hover,input[type=submit].button-primary:focus,input[type=submit].button-primary:hover{color:#fff;background-color:#1eaedb;border-color:#1eaedb}input[type=email],input[type=number],input[type=password],input[type=search],input[type=tel],input[type=text],input[type=url],select,textarea{height:38px;padding:6px 10px;background-color:#fff;border:1px solid #d1d1d1;border-radius:4px;box-shadow:none;box-sizing:border-box}input[type=email],input[type=number],input[type=password],input[type=search],input[type=tel],input[type=text],input[type=url],textarea{-webkit-appearance:none;-moz-appearance:none;appearance:none}textarea{min-height:65px;padding-top:6px;padding-bottom:6px}input[type=email]:focus,input[type=number]:focus,input[type=password]:focus,input[type=search]:focus,input[type=tel]:focus,input[type=text]:focus,input[type=url]:focus,select:focus,textarea:focus{border:1px solid #33c3f0;outline:0}label,legend{display:block;margin-bottom:.5rem;font-weight:600}fieldset{padding:0;border-width:0}input[type=checkbox],input[type=radio]{display:inline}label>.label-body{display:inline-block;margin-left:.5rem;font-weight:400}ul{list-style:circle inside}ol{list-style:decimal inside}ol,ul{padding-left:0;margin-top:0}ol ol,ol ul,ul ol,ul ul{margin:1.5rem 0 1.5rem 3rem;font-size:90%}li{margin-bottom:1rem}code{padding:.2rem .5rem;margin:0 .2rem;font-size:90%;white-space:nowrap;background:#f1f1f1;border:1px solid #e1e1e1;border-radius:4px}pre>code{display:block;padding:1rem 1.5rem;white-space:pre}td,th{padding:12px 15px;text-align:left;border-bottom:1px solid #e1e1e1}td:first-child,th:first-child{padding-left:0}td:last-child,th:last-child{padding-right:0}.button,button{margin-bottom:1rem}fieldset,input,select,textarea{margin-bottom:1.5rem}blockquote,dl,figure,form,ol,p,pre,table,ul{margin-bottom:2.5rem}.u-full-width{width:100%;box-sizing:border-box}.u-max-full-width{max-width:100%;box-sizing:border-box}.u-pull-right{float:right}.u-pull-left{float:left}hr{margin-top:3rem;margin-bottom:3.5rem;border-width:0;border-top:1px solid #e1e1e1}.container:after,.row:after,.u-cf{content:\"\";display:table;clear:both}");

        lineaCon("</style>");
        lineaCon("</head>");
        lineaCon("<body>");
        inicioPanel();

        tituloCon("Documentación");
        parrafoCon("Para describir cada uno de los Tipos de datos se utilizará la siguiente estructura:");
        separador();

        inicioPanel();
        subtituloCon("Tipo");
        parrafoCon("Nombre del Tipo de dato.");
        seccionCon("Descripción");
        parrafoCon("Propósito del Tipo. Rol que cumple dentro de la Batalla Espacial.");
        seccionCon("Valores");
        parrafoCon("Valores posibles para aquellos tipos que son enumerados.");
        seccionCon("Constructores");
        parrafoCon("Lista de todos los constructores disponibles para crear objetos de ese Tipo.");
        parrafoCon("Se indica el Tipo de los valores que recibe como parámetro.");
        seccionCon("Métodos");
        parrafoCon("Lista de todos los métodos disponibles para ese Tipo.");
        parrafoCon("Se indica el Tipo de valor que retorna, el nombre del método y los Tipos de valores que recibe como parámetro.");
        parrafoCon("Se utiliza la palabra “<strong>void</strong>” para aquellos métodos que no devuelven ningún valor (void se puede traducir como vacío)");
        parrafoCon("Además se indican la pre y post-condición de cada método.");
        parrafoCon("La <strong>pre-condición</strong> (si la tiene) es lo que debo cumplir antes de invocarlo.");
        parrafoCon("La <strong>post-condición</strong> es lo que se cumple luego de ejecutarlo.");
        finPanel();
    }

    public void escribirPie() {

        finPanel();
        lineaCon("</body>");
    }

    private PrintStream abrirArchivo(String conRuta) {

        try {

            Path ruta = Paths.get(conRuta);
            Files.createDirectories(ruta.getParent());

            return new PrintStream(Files.newOutputStream(ruta,
                    StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE));

        } catch (Exception e) {

            throw new RuntimeException(String.format("No fue posible abrir el archivo '%s'", conRuta), e);
        }
    }

    public void terminar() {

        archivo.close();
    }

    public void escribirTitulo(String titulo) {

        tituloCon(titulo);
    }

    public void escribirTipo(String nombre) {

        separador();
        subtituloCon("Tipo");
        codigoCon(nombre);

        escribioUnConstructor.set(false);
        escribioUnMetodo.set(false);
    }

    public void escribirDescripcion(String texto) {

        seccionCon("Descripción");
        parrafoCon(texto);
    }

    public void escribirConstructor(String codigo) {

        if (!escribioUnConstructor.getAndSet(true)) {

            seccionCon("Constructores");
        }

        operacionCon(codigo);
    }

    public void escribirMetodo(String codigo) {

        if (!escribioUnMetodo.getAndSet(true)) {

            seccionCon("Métodos");
        }

        operacionCon(codigo);
    }

    public void escribirPre(String texto) {

        condicionCon("pre", texto);
    }

    public void escribirPost(String texto) {

        condicionCon("post", texto);
    }

    public void escribirRetorno(String texto) {

        condicionCon("retorno", texto);
    }

    public void escribirParametro(String nombre, String descripcion) {

        condicionCon("&nbsp;", nombre + ": " + descripcion);
    }

    public void escribirValoresEnumerados(List<String> valores) {

        seccionCon("Valores");
        inicioFila();
        lineaCon("<div class=\"six columns\">");

        for (String valor : valores) {

            codigoCon(valor);
        }

        lineaCon("</div>");
        lineaCon("<div class=\"six columns\">&nbsp;</div>");
        finFila();
    }
}
