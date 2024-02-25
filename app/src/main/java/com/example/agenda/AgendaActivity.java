package com.example.agenda;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgendaActivity extends AppCompatActivity {
    CalendarView cal;
    ListView listEvents;
    Map<Long, List<String>> events = new HashMap<>();
    ArrayAdapter<String> adapter;
    EditText addEventEditText;
    Button addButton;
    long selectedDateInMillis;

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

        cal.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Formater la date sélectionnée
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(year, month, dayOfMonth);
            selectedDateInMillis = selectedDate.getTimeInMillis();

            // Rafraîchir la liste des événements pour la date sélectionnée
            refreshEventsForSelectedDate(selectedDateInMillis);
        });

        addButton.setOnClickListener(v -> {
            String event = addEventEditText.getText().toString();
            if (!event.isEmpty()) {
                // Ajouter l'événement à la liste des événements associés à la date sélectionnée
                addEvent(selectedDateInMillis, event);

                // Rafraîchir la liste des événements pour afficher tous les événements associés à cette date
                refreshEventsForSelectedDate(selectedDateInMillis);

                // Effacer le champ de saisie
                addEventEditText.setText("");

                Toast.makeText(AgendaActivity.this, "Événement ajouté avec succès !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Méthode pour ajouter un événement pour une date spécifique
    private void addEvent(long dateInMillis, String event) {
        // Vérifier si des événements existent déjà pour cette date
        List<String> existingEvents = events.get(dateInMillis);
        if (existingEvents == null) {
            // Aucun événement pour cette date, créer une nouvelle liste d'événements
            existingEvents = new ArrayList<>();
            events.put(dateInMillis, existingEvents);
        }

        // Ajouter l'événement à la liste
        existingEvents.add(event);
    }

    // Méthode pour rafraîchir les événements pour la date sélectionnée
    private void refreshEventsForSelectedDate(long dateInMillis) {
        // Récupérer les événements associés à cette date
        List<String> eventsForDate = events.get(dateInMillis);
        adapter.clear();
        if (eventsForDate != null && !eventsForDate.isEmpty()) {
            // Mettre à jour l'adaptateur de la liste pour afficher les événements
            adapter.addAll(eventsForDate);
        } else {
            // Aucun événement pour cette date, afficher un message
            Toast.makeText(AgendaActivity.this, "Aucun événement pour cette date", Toast.LENGTH_SHORT).show();
        }
    }
}
