package org.cbhs.stem.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.cbhs.stem.model.Opportunity;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads extended learning opportunities from data/opportunities.json.
 */
public class JsonOpportunityRepository {
    private final List<Opportunity> opportunities;

    public JsonOpportunityRepository() {
        this.opportunities = loadOpportunities();
    }

    public List<Opportunity> findAll() {
        return new ArrayList<>(opportunities);
    }

    private List<Opportunity> loadOpportunities() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("data/opportunities.json");
        if (input == null) {
            throw new IllegalStateException("Could not find data/opportunities.json");
        }

        Gson gson = new Gson();
        Type type = new TypeToken<OpportunityDataFile>() {}.getType();
        OpportunityDataFile data = gson.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), type);
        return data.opportunities;
    }

    private static class OpportunityDataFile {
        private List<Opportunity> opportunities;
    }
}
