package espacial.documentador;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

class DocumentacionTest {

    private static final String RUTA_BASE = "./target/test-result/";
    private Documentacion documentacion;
    private String ruta;

    @BeforeEach
    void definirRuta(TestInfo testInfo) throws IOException {

        ruta = RUTA_BASE + testInfo.getTestMethod().get().getName() + ".html";
        Files.deleteIfExists(Paths.get(ruta));
    }

    @Test
    void terminar() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.terminar();

        comprobarQueLaDocumentacionFueGenerada();
    }

    @Test
    void escribirTitulo() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirTitulo("Documentación de prueba");
        documentacion.terminar();

        comprobarQueContiene("<h1>Documentación de prueba</h1>");
    }

    @Test
    void escribirTipo() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirTipo("TipoEspacial");
        documentacion.terminar();

        comprobarQueContiene("<hr></hr>","<h3>Tipo</h3>","<pre><code>TipoEspacial</code></pre>");
    }

    @Test
    void escribirDescripcion() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirDescripcion("Vehículo espacial que...");
        documentacion.terminar();

        comprobarQueContiene("<h4>Descripción</h4>","<p>Vehículo espacial que...</p>");
    }

    @Test
    void escribirConstructor() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirConstructor("Base(String nombre, int cantidad)");
        documentacion.terminar();

        comprobarQueContiene(
                "<h4>Constructores</h4>",
                "<div class=\"row\">",
                "<div class=\"one column\">&nbsp;</div>",
                "<div class=\"eleven columns\">",
                "<pre><code>Base(String nombre, int cantidad)</code></pre>",
                "</div>",
                "</div>");
    }

    @Test
    void escribirMetodo() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirMetodo("void cargarDesdeEste(Sustancia sustancia, int cantidad)");
        documentacion.terminar();

        comprobarQueContiene(
                "<h4>Métodos</h4>",
                "<div class=\"row\">",
                "<div class=\"one column\">&nbsp;</div>",
                "<div class=\"eleven columns\">",
                "<pre><code>void cargarDesdeEste(Sustancia sustancia, int cantidad)</code></pre>",
                "</div>",
                "</div>");
    }

    @Test
    void escribirConstructorMultiplesVeces() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirConstructor("Base(String nombre, int cantidad)");
        documentacion.escribirConstructor("Base(String nombre)");
        documentacion.terminar();

        comprobarQueContiene(
                "<h4>Constructores</h4>",
                "<div class=\"row\">",
                "<div class=\"one column\">&nbsp;</div>",
                "<div class=\"eleven columns\">",
                "<pre><code>Base(String nombre, int cantidad)</code></pre>",
                "</div>",
                "</div>",
                "<div class=\"row\">",
                "<div class=\"one column\">&nbsp;</div>",
                "<div class=\"eleven columns\">",
                "<pre><code>Base(String nombre)</code></pre>",
                "</div>",
                "</div>"
        );
    }

    @Test
    void escribirPre() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirPre("fue creada la BatallaEspacial");
        documentacion.terminar();

        comprobarQueContiene(
                "<div class=\"row\">",
                "<div class=\"two columns\">&nbsp;</div>",
                "<div class=\"two columns\"><strong>pre</strong></div>",
                "<div class=\"eight columns\">fue creada la BatallaEspacial</div>",
                "</div>");
    }

    @Test
    void escribirPost() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirPost("avanza al SUR");
        documentacion.terminar();

        comprobarQueContiene(
                "<div class=\"row\">",
                "<div class=\"two columns\">&nbsp;</div>",
                "<div class=\"two columns\"><strong>post</strong></div>",
                "<div class=\"eight columns\">avanza al SUR</div>",
                "</div>");
    }

    @Test
    void escribirRetorno() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirRetorno("porcentaje comprendido entre [0..100]");
        documentacion.terminar();

        comprobarQueContiene(
                "<div class=\"row\">",
                "<div class=\"two columns\">&nbsp;</div>",
                "<div class=\"two columns\"><strong>return</strong></div>",
                "<div class=\"eight columns\">porcentaje comprendido entre [0..100]</div>",
                "</div>");
    }

    @Test
    void escribirParametro() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirParametro("sustancia", "tipifica la carga");
        documentacion.terminar();

        comprobarQueContiene(
                "<div class=\"row\">",
                "<div class=\"two columns\">&nbsp;</div>",
                "<div class=\"two columns\"><strong>param</strong></div>",
                "<div class=\"eight columns\">sustancia: tipifica la carga</div>",
                "</div>");
    }

    @Test
    void escribirValoresEnumerados() {

        dadoQueFueCreadaLaDocumentacion();

        documentacion.escribirValoresEnumerados(Arrays.asList("ANTIMATERIA", "METAL", "CRISTAL"));
        documentacion.terminar();

        comprobarQueContiene(
                "<h4>Valores</h4>",
                "<div class=\"row\">",
                "<div class=\"six columns\">",
                "<pre><code>ANTIMATERIA</code></pre>",
                "<pre><code>METAL</code></pre>",
                "<pre><code>CRISTAL</code></pre>",
                "</div>",
                "<div class=\"six columns\">&nbsp;</div>",
                "</div>"
        );
    }

    private void dadoQueFueCreadaLaDocumentacion() {

        documentacion = new Documentacion(ruta);
    }

    private void comprobarQueLaDocumentacionFueGenerada() {

        assertThat(abrirArchivo()).exists();
    }

    private void comprobarQueContiene(String contenido) {

        assertThat(abrirArchivo()).hasContent(contenido);
    }

    private void comprobarQueContiene(String... lineas) {

        assertThat(abrirArchivo()).hasContent(Stream.of(lineas).collect(Collectors.joining("\n")));
    }

    private File abrirArchivo() {

        return new File(ruta);
    }
}