package ja.fr.installationssportives;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ja.fr.installationssportives.modele.Infrastructure;


/**
 * A simple {@link Fragment} subclass.
 */
public class Infrastructures extends Fragment implements AdapterView.OnItemClickListener{

    private List<Infrastructure> infraList;
    private ListView infraListView;

    public Infrastructures() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_infrastructures, container, false);

        infraListView = view.findViewById(R.id.infraListView);

        infraListView.setOnItemClickListener(this);

        getDataFromHttp();

        return view;
    }

    private void processResponse(String response){
        //Transformation de la réponse json en list de RandomUser
        infraList = responseToList(response);

        //Conversion de la liste d'infrastructure en un tableau de String comportant
        //uniquement le nom des infrastructures
        String[] data = new String[infraList.size()];
        for(int i=0; i < infraList.size(); i++){
            data[i] = infraList.get(i).getName();
        }

        //Définition d'un ArrayAdapter pour alimenter la ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this.getActivity(),
                android.R.layout.simple_list_item_1,
                data
        );

        infraListView.setAdapter(adapter);
    }


    private void getDataFromHttp(){
        String url = "https://opendata.lillemetropole.fr/api/records/1.0/search/?dataset=installation-sportives&facet=cominsee&facet=comlib&apikey=26b2037502612840f522fffe95f1ac13d812eb5715586b6dc63250c8";

        //Définition de la requête
        StringRequest request = new StringRequest(
                //Méthode de la requête http
                Request.Method.GET,
                url,
                //Gestionnaire de succès
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("HTTP", response);
                        processResponse(response);
                    }
                },

                //Gestionnaire d'erreur
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("HTTP",error.getMessage());
                    }
                }
        );

        //Ajout de la requête à la file d'éxécution
        Volley.newRequestQueue(this.getActivity())
                .add(request);
    }

    // Conversion d'une réponse json (chaine de caractères)
    // en une liste d'infrastructures
    private List<Infrastructure> responseToList(String response){
        List<Infrastructure> list = new ArrayList<>();

        try {
            JSONObject jsonInfras = new JSONObject(response);
            JSONArray records = jsonInfras.getJSONArray("records");
            JSONObject item;

            for (int i = 0; i < records.length(); i++){
                item = (JSONObject) records.get(i);

                //Création d'une nouvelle infrastructure
                Infrastructure infra = new Infrastructure();

                //Hydratation de l'infrastructure

                JSONObject fields = item.getJSONObject("fields");

                infra.setName(fields.getString("insnom"));
                infra.setAdress(fields.getString("inslibellevoie"));
                infra.setNbEquipements(Integer.valueOf(fields.getString("nb_equipements")));

                // Récupération des coordonnées de latitude et longitude
                JSONArray geo = fields.getJSONArray("geo");

                    infra.setLatitude(geo.getDouble(0));
                    infra.setLongitude(geo.getDouble(1));

                // ajout de l'utilisateur à la liste
                list.add(infra);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //Récupération de l'infrastructure sur laquelle on vient de cliquer
        Infrastructure selectedInfra = this.infraList.get(position);

        //Création d'une intention pour l'affichage de la carte
        Intent mapIntention = new Intent(this.getActivity(), MapsActivity.class);


        //Passage des paramètres
        mapIntention.putExtra("latitude", selectedInfra.getLatitude());
        mapIntention.putExtra("longitude", selectedInfra.getLongitude());


        //Passage des paramètres d'info
        mapIntention.putExtra("libelle_voie", selectedInfra.getAdress());
        mapIntention.putExtra("nb_equipements", selectedInfra.getNbEquipements());

        //Affichage de l'activité
        startActivity(mapIntention);
    }
}
