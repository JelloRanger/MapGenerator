package procedural;

import model.City;
import model.LocationType;
import model.Terrain;

import java.util.*;
import java.util.Map;

public class NameGeneration {

    private final int MIN_NAME_LENGTH = 5;

    private final int MAX_NAME_LENGTH = 10;

    private List<String> mNames = new ArrayList<String>() {{
        add("china");
        add("india");
        add("unitedstates");
        add("indonesia");
        add("brazil");
        add("pakistan");
        add("nigeria");
        add("bangladesh");
        add("russia");
        add("japan");
        add("mexico");
        add("philippines");
        add("ethiopia");
        add("vietnam");
        add("egypt");
        add("iran");
        add("germany");
        add("turkey");
        add("congodemrep");
        add("thailand");
    }};

    private Map<Character, List<Character>> mTransitions;

    private List<String> mRandomNames;

    private map.Map mMap;

    private int numNames;

    public NameGeneration(map.Map map, int amount) {
        mMap = map;
        numNames = amount;
    }

    public void generate() {
        mTransitions = new HashMap<>();

        for (String name : mNames) {
            addToTransitions(name);
        }

        generateRandomNames();
        assignNamesToCities();
    }

    private void addToTransitions(String name) {
        for (int i = 0; i < name.length() - 1; i++) {
            if (mTransitions.containsKey(name.charAt(i))) {
                mTransitions.get(name.charAt(i)).add(name.charAt(i + 1));
            } else {
                mTransitions.put(name.charAt(i), new ArrayList<>(Collections.singletonList(name.charAt(i + 1))));
            }
        }
    }

    private void generateRandomNames() {
        mRandomNames = new ArrayList<>();

        for (int i = 0; i < numNames; i++) {
            mRandomNames.add(generateRandomName());
        }
    }

    private void assignNamesToCities() {
        Queue<String> queue = new LinkedList<>(mRandomNames);
        for (int y = 0; y < mMap.getHeight(); y++) {
            for (int x = 0; x< mMap.getWidth(); x++) {
                Terrain terrain = mMap.getTerrain(x, y);
                if (terrain.getLocationType().equals(LocationType.CITY)) {
                    terrain.setLocation(new City(queue.poll()));
                }
            }
        }
    }

    private String generateRandomName() {
        StringBuilder sb = new StringBuilder();
        char firstCharacter = randomCharacter();
        sb.append(Character.toUpperCase(firstCharacter));

        int nameLength = new Random().nextInt(MAX_NAME_LENGTH - MIN_NAME_LENGTH) + MIN_NAME_LENGTH;
        char currentCharacter = firstCharacter;
        while (hasNextCharacter(currentCharacter) && sb.length() < nameLength) {
            currentCharacter = generateNextCharacter(currentCharacter);
            sb.append(currentCharacter);
        }

        return sb.toString();
    }

    private char randomCharacter() {
        List<Character> allCharacters = new ArrayList<>(mTransitions.keySet());
        Collections.shuffle(allCharacters);
        return allCharacters.get(0);
    }

    private char generateNextCharacter(char c) {

        Collections.shuffle(mTransitions.get(c));
        return mTransitions.get(c).get(0);
    }

    private boolean hasNextCharacter(char c) {
        return mTransitions.containsKey(c);
    }
}
