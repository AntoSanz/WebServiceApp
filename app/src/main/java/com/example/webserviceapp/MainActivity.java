package com.example.webserviceapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;


public class MainActivity extends AppCompatActivity {

    static final String SOAPACTION = "http://webservices/getLetraNif";
    private static final String METHOD = "getLetraNif";
    private static final String NAMESPACE = "http://webservices/";
    private static final String URL = "http://172.16.1.139:8087/ProyectoServicioWeb/ServicioDNI?xsd";


    EditText txtdni;
    TextView lblletra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtdni = (EditText) findViewById(R.id.txtdni);
        lblletra = (TextView) findViewById(R.id.txtletra);

    }

    public void mostrarLetra(View v) {
        try {
        /*Lo recomendado es crear esa tarea en un subproceso o hilo secundario, no obstante, si necesitáis hacerlo a la fuerza, se puede establecer un cambio en las políticas de restricciones de Android para nuestra clase (repito, no es recomendable).
        Lo único que habría que hacer es insertar estas dos líneas de código en el onCreate() de nuestra clase principal, y Android se tragará cualquier acceso a red que hagamos en el Main Thread, sin rechistar
        */
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();

            StrictMode.setThreadPolicy(policy);

            String resultadoFINAL;
            //Creacion de la Solicitud
            SoapObject request = new SoapObject(NAMESPACE, METHOD);
            //Creacion del Envelope
            SoapSerializationEnvelope sobre = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            sobre.setOutputSoapObject(request);
            //Creacion del transporte
            HttpTransportSE transporte = new HttpTransportSE(URL);

            // Paso de parámetro
            PropertyInfo dni = new PropertyInfo();
            dni.setName("numerodni");
            dni.setValue(this.txtdni.getText().toString());
            dni.setType(Integer.class);
            request.addProperty(dni);

            //Llamada
            transporte.call(SOAPACTION, sobre);

            //Resultado
            SoapPrimitive resultado = (SoapPrimitive) sobre.getResponse();
            resultadoFINAL = "LETRA: " + resultado.toString();
            lblletra.setText(resultadoFINAL);
        } catch (Exception e) {
            lblletra.setText(e.toString());
        }
    }
}