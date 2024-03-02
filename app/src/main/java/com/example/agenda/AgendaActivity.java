package com.example.agenda;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AgendaActivity extends AppCompatActivity {
    CalendarView cal;
    ListView listEvents;
    Map<String, List<String>> events = new HashMap<>();
    ArrayAdapter<String> adapter;
    EditText addEventEditText;
    Button addButton;
    String selectedDate; // Variable pour stocker la date sélectionnée dans le CalendarView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agenda);

        cal = findViewById(R.id.calendar);
        listEvents = findViewById(R.id.list_events);
        addEventEditText = findViewById(R.id.add_events_text_view);
        addButton = findViewById(R.id.add_btn);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listEvents.setAdapter(adapter);

        // Ajoutez un écouteur de changement de date au CalendarView
        cal.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Formatez la date sélectionnée au format "jour-mois-année"
            selectedDate = formatDate(year, month, dayOfMonth);

            // Rafraîchissez la liste des événements pour la date sélectionnée
            refreshEventsForSelectedDate(selectedDate);
        });

        addButton.setOnClickListener(v -> {
            String event = addEventEditText.getText().toString();
            if (!event.isEmpty()) {
                // Ajoutez l'événement à la liste des événements associés à la date sélectionnée
                addEvent(selectedDate, event);

                // Rafraîchissez la liste des événements pour afficher tous les événements associés à cette date
                refreshEventsForSelectedDate(selectedDate);

                // Effacez le champ de saisie
                addEventEditText.setText("");

                Toast.makeText(AgendaActivity.this, "Événement ajouté avec succès !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour ajouter un événement pour une date spécifique
    private void addEvent(String date, String event) {
        // Vérifiez si des événements existent déjà pour cette date
        List<String> existingEvents = events.get(date);
        if (existingEvents == null) {
            // Aucun événement pour cette date, créez une nouvelle liste d'événements
            existingEvents = new ArrayList<>();
            events.put(date, existingEvents);
        }

        // Ajoutez l'événement à la liste
        existingEvents.add(event);
    }

    // Méthode pour rafraîchir les événements pour la date sélectionnée
    private void refreshEventsForSelectedDate(String selectedDate) {
        // Récupérez les événements associés à cette date
        List<String> eventsForDate = events.get(selectedDate);
        adapter.clear();
        if (eventsForDate != null && !eventsForDate.isEmpty()) {
            // Mettez à jour l'adaptateur de la liste pour afficher les événements
            adapter.addAll(eventsForDate);
        } else {
            // Aucun événement pour cette date, affichez un message
            Toast.makeText(AgendaActivity.this, "Aucun événement pour cette date", Toast.LENGTH_SHORT).show();
        }
    }

    // Méthode pour formater une date au format "jour-mois-année"
    private String formatDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }
}
