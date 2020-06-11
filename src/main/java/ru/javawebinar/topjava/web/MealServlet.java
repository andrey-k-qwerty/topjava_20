package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.filteredByStreams;
import static ru.javawebinar.topjava.util.TimeUtil.formatter;

//@WebServlet(name = "MealServlet")
public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static String INSERT_OR_EDIT = "/editMeal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealRepository repository;

    @Override
    public void init() throws ServletException {
        super.init();
        repository = new MealRepository();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // log.debug("forward to {}",forward);
        String action = request.getParameter("action");
        String forward = "";
        Integer id;
        if (action == null) {
            action = "";
        }

        switch (action.toLowerCase()) {
            case "add":
                forward = INSERT_OR_EDIT;
                request.setAttribute("meal", new Meal(LocalDateTime.now(),"",0));
                break;
            case "edit":
                forward = INSERT_OR_EDIT;
                id = getId( request.getParameter( "Id"));
                final Meal meal = repository.get(id);
                request.setAttribute("meal", meal);
                break;
            case "delete":
                id = getId( request.getParameter( "Id"));
                repository.delete(id);
              //  break;
            default:
                forward = LIST_MEAL;
                final List<MealTo> all = filteredByStreams(repository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
                request.setAttribute("meals", all);
        }


        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String date = req.getParameter("date");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");
        Integer id = getId( req.getParameter( "id"));
        repository.add(new Meal(id, LocalDateTime.parse(date,formatter),description,Integer.parseInt(calories)));
        List<MealTo> allListMealTo = MealsUtil.filteredByStreams(repository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY);
        req.setAttribute("meals", allListMealTo);
        req.getRequestDispatcher(LIST_MEAL).forward(req, resp);

    }

    private int getId(String id) {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
           return 0;
        }
    }
}
