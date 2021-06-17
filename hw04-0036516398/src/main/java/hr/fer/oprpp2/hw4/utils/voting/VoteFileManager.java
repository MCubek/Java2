package hr.fer.oprpp2.hw4.utils.voting;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * Helper class containg static methods for reading and writing to
 * votes and bands files.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
public class VoteFileManager {
    /**
     * Object used for monitor lock when accesing votes file.
     */
    private static final Object votesLock = new Object();

    /**
     * Method used for parsing bands file to a map containing all bands and their id's.
     * Map is sorted by band id.
     *
     * @param bands path with bands file
     * @return map containing bands and their id's sorted by id.
     * @throws IOException when error ocurs with reading bands file.
     */
    public synchronized static Map<Long, Band> getBands(Path bands) throws IOException {
        Map<Long, Band> bandMap = new TreeMap<>();
        for (String line : Files.readAllLines(bands)) {
            String[] split = line.split("\t");
            if (split.length != 3) continue;
            long id = Long.parseLong(split[0]);
            String name = split[1];
            String url = split[2];
            bandMap.put(id, new Band(id, name, url));
        }
        return bandMap;
    }

    /**
     * Method used for getting votes and bands map.
     * Map is not sorted.
     * Method is thread safe.
     *
     * @param bands bands file path
     * @param votes votes path
     * @return unsorted map of bands and their votes
     * @throws IOException when error ocurs while reading votes or bands file
     */
    public static Map<Band, Integer> getVotes(Path bands, Path votes) throws IOException {
        var bandsMap = getBands(bands);
        Map<Band, Integer> bandVotes = new HashMap<>();
        synchronized (votesLock) {
            if (! Files.exists(votes))
                Files.createFile(votes);

            for (String line : Files.readAllLines(votes)) {
                String[] split = line.split("\t");
                if (split.length != 2) continue;
                long id = Long.parseLong(split[0]);
                int voteNumber = Integer.parseInt(split[1]);
                Band band = bandsMap.get(id);
                if (band != null)
                    bandVotes.put(band, voteNumber);
            }

        }
        bandsMap.forEach((k, v) -> {
            if (! bandVotes.containsKey(v)) {
                bandVotes.put(v, 0);
            }
        });
        return bandVotes;
    }

    /**
     * Map used for getting votes and bands map.
     * Map retrieved is sorted by number of votes descending.
     * Method is thread safe.
     *
     * @param bands bands file path
     * @param votes votes file path
     * @return sorted map of bands and their votes
     * @throws IOException when error ocurs while reading votes or bands file
     */
    public static Map<Band, Integer> getSortedVotes(Path bands, Path votes) throws IOException {
        return sortMapByValueDescending(getVotes(bands, votes));
    }

    /**
     * Method used for adding vote to votes file.
     * Method is thread safe.
     *
     * @param votes  votes file path
     * @param bandId band id to vote for
     * @throws IOException when error ocurs while writing or reading votes file
     */
    public static void addVote(Path votes, long bandId) throws IOException {
        Map<Long, Integer> bandVotes = new HashMap<>();
        synchronized (votesLock) {
            //Create file if doesn't exist
            if (! Files.exists(votes))
                Files.createFile(votes);

            //Read file
            for (String line : Files.readAllLines(votes)) {
                String[] split = line.split("\t");
                if (split.length != 2) continue;
                long id = Long.parseLong(split[0]);
                int voteNumber = Integer.parseInt(split[1]);
                bandVotes.put(id, voteNumber);
            }

            //Add vote
            int voteNumber = bandVotes.getOrDefault(bandId, 0);
            bandVotes.put(bandId, voteNumber + 1);

            //Rewrite file
            try (BufferedWriter bw = Files.newBufferedWriter(votes, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                bandVotes.forEach((k, v) -> {
                    try {
                        bw.write(String.format("%d\t%d\n", k, v));
                    } catch (IOException ignore) {
                    }
                });
            }
        }
    }

    /**
     * Helper method for sorting Map by value by descending natural oreder.
     *
     * @param map map to sort
     * @param <K> key type
     * @param <V> value type, has to be Comparable with itself
     * @return map with same elements but sorted by value descending
     */
    private static <K, V extends Comparable<V>> Map<K, V> sortMapByValueDescending(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        Map<K, V> sortedMap = new LinkedHashMap<>();
        for (var entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
