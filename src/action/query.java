package action;

import actor.ActorsAwards;
import database.*;
import fileio.ActionInputData;
import fileio.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public final class query {

    public static String userByNoRatings(ActionInputData action, DB db) {
        ArrayList<Users> l = new ArrayList<>(db.getUsers());
        l.removeIf((e) -> e.getNumberOfRatings() == 0);
        if (action.getSortType().equals("desc"))
            l.sort((o1, o2) -> {
                if (o2.getNumberOfRatings() - o1.getNumberOfRatings() == 0)
                    return o2.getUsername().compareTo(o1.getUsername());
                return o2.getNumberOfRatings() - o1.getNumberOfRatings();
            });
        else
            l.sort((o2, o1) -> {
                if (o2.getNumberOfRatings() - o1.getNumberOfRatings() == 0)
                    return o2.getUsername().compareTo(o1.getUsername());
                return o2.getNumberOfRatings() - o1.getNumberOfRatings();
            });

        return "Query result: " + (ArrayList<String>)
                l.stream().map(Users::getUsername).limit(action.getNumber()).collect(Collectors.toList());
    }

    public static String actors(ActionInputData action, DB db) {
        ArrayList<Actors> l = new ArrayList<>(db.getActors());
        switch (action.getCriteria()) {
            case "average" -> {
                l.removeIf((e) -> e.AvgRating(db) == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((o1, o2) -> {
                        if (o2.AvgRating(db).compareTo(o1.AvgRating(db)) == 0)
                            return o1.getName().compareTo(o2.getName());
                        return -o2.AvgRating(db).compareTo(o1.AvgRating(db));
                    });
                else
                    db.getActors().sort((o1, o2) -> {
                        if (o2.AvgRating(db).compareTo(o1.AvgRating(db)) == 0)
                            return o2.getName().compareTo(o1.getName());
                        return -o2.AvgRating(db).compareTo(o1.AvgRating(db));
                    });
            }
            case "awards" -> {
                action.getFilters().get(3).forEach((award) -> {
                    l.removeIf((actor) -> !actor.getAwards().containsKey(ActorsAwards.valueOf(award)));
                });

                if (action.getSortType().equals("desc"))
                    l.sort((a1, a2) -> {
                        if (a2.getAwardsNumber() - a1.getAwardsNumber() == 0)
                            return a2.getName().compareTo(a1.getName());
                        return a2.getAwardsNumber() - a1.getAwardsNumber();
                    });
                else
                    l.sort((a1, a2) -> {
                        if (a2.getAwardsNumber() - a1.getAwardsNumber() == 0)
                            return a1.getName().compareTo(a2.getName());
                        return a1.getAwardsNumber() - a2.getAwardsNumber();
                    });

            }
            case "filter_description" -> {

                action.getFilters().get(2).forEach((word) -> {
                    l.removeIf((actor) -> !actor.getCareerDescription().contains(word));
                });
                if (action.getSortType().equals("asc"))
                    l.sort((a1, a2) -> a1.getName().compareTo(a2.getName()));
                else
                    l.sort((a2, a1) -> a1.getName().compareTo(a2.getName()));
            }
        }
        return "Query result: " + (ArrayList<String>)
                l.stream().map(Actors::getName).limit(action.getNumber()).collect(Collectors.toList());
    }

    public static String movies(ActionInputData action, DB db) {
        ArrayList<Movies> l = new ArrayList<>(db.getMovies());
        if (!action.getFilters().get(0).isEmpty()) {
            action.getFilters().get(0).forEach((year) -> {
                if (year != null)
                    l.removeIf((movie) -> movie.getYear() != Integer.parseInt(year));
            });
        }
        if (!action.getFilters().get(1).isEmpty()) {
            action.getFilters().get(1).forEach((genre) -> {
                if (genre != null)
                    l.removeIf((movie) -> !movie.getGenres().contains(genre));
            });
        }
        switch (action.getCriteria()) {
            case "ratings" -> {
                l.removeIf((movie) -> movie.getAverageRating() == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((m1, m2) -> m1.getAverageRating().compareTo(m2.getAverageRating()));
                else
                    l.sort((m2, m1) -> m1.getAverageRating().compareTo(m2.getAverageRating()));
            }
            case "favorite" -> {
                l.removeIf((m) -> m.getNumberOfFavorites(db) == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((m1, m2) -> {
                        if (m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db) == 0)
                            return m1.getTitle().compareTo(m2.getTitle());
                        return m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db);
                    });
                else
                    l.sort((m2, m1) -> {
                        if (m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db) == 0)
                            return m1.getTitle().compareTo(m2.getTitle());
                        return m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db);
                    });

            }
            case "longest" -> {
                if (action.getSortType().equals("desc"))
                    l.sort((m1, m2) -> m2.getDuration() - m1.getDuration());
                else
                    l.sort((m1, m2) -> m1.getDuration() - m2.getDuration());
            }
            case "most_viewed" -> {
                l.removeIf((m) -> m.getViews(db) == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((m1, m2) -> m1.getViews(db) - m2.getViews(db));
                else
                    l.sort((m2, m1) -> m1.getViews(db) - m2.getViews(db));

            }
        }
        return "Query result: " + (ArrayList<String>)
                l.stream().map(Movies::getTitle).limit(action.getNumber()).collect(Collectors.toList());

    }

    public static String shows(ActionInputData action, DB db) {
        ArrayList<Shows> l = new ArrayList<>(db.getShows());
        action.getFilters().get(0).forEach((year) -> {
            if (year != null)
                l.removeIf((show) -> show.getYear() != Integer.parseInt(year));
        });
        action.getFilters().get(1).forEach((genre) -> {
            if (genre != null)
                l.removeIf((show) -> !show.getGenres().contains(genre));
        });

        switch (action.getCriteria()) {
            case "ratings" -> {
                l.removeIf((show) -> show.getAverageRating() == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((m1, m2) -> m1.getAverageRating().compareTo(m2.getAverageRating()));
                else
                    l.sort((m2, m1) -> m1.getAverageRating().compareTo(m2.getAverageRating()));
            }
            case "favorite" -> {
                l.removeIf((s) -> s.getNumberOfFavorites(db) == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((m1, m2) -> m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db));
                else
                    l.sort((m2, m1) -> m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db));

            }
            case "longest" -> {
                if (action.getSortType().equals("desc"))
                    l.sort((m1, m2) -> m2.getDuration() - m1.getDuration());
                else
                    l.sort((m1, m2) -> m1.getDuration() - m2.getDuration());
            }
            case "most_viewed" -> {
                l.removeIf((m) -> m.getViews(db) == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((m1, m2) -> m1.getViews(db) - m2.getViews(db));
                else
                    l.sort((m2, m1) -> m1.getViews(db) - m2.getViews(db));

            }
        }
        return "Query result: " + (ArrayList<String>)
                l.stream().map(Shows::getTitle).limit(action.getNumber()).collect(Collectors.toList());

    }

    public static Object execute(ActionInputData action, DB db, Writer writer) throws IOException {
        String msg = "";
        switch (action.getObjectType()) {
            case "actors" -> {
                msg = actors(action, db);
            }
            case "shows" -> {
                msg = shows(action, db);
            }
            case "movies" -> {
                msg = movies(action, db);
            }
            case "users" -> {
                msg = userByNoRatings(action, db);
            }
        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
