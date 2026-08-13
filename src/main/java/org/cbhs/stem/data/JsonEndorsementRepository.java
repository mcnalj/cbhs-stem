package org.cbhs.stem.data;

import com.google.gson.Gson;
import org.cbhs.stem.model.Endorsement;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * Loads STEM endorsement requirements from data/endorsement.json.
 */
public class JsonEndorsementRepository {
    private final Endorsement endorsement;

    public JsonEndorsementRepository() {
        this.endorsement = loadEndorsement();
    }

    public Endorsement getEndorsement() {
        return endorsement;
    }

    private Endorsement loadEndorsement() {
        InputStream input = getClass().getClassLoader().getResourceAsStream("data/endorsement.json");
        if (input == null) {
            throw new IllegalStateException("Could not find data/endorsement.json");
        }

        Gson gson = new Gson();
        return gson.fromJson(new InputStreamReader(input, StandardCharsets.UTF_8), Endorsement.class);
    }
}
