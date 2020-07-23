package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller()
@RequestMapping("/meals")
public class JspMealController extends BaseMealController {


    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String list(Model model) {
        log.info("Controller - list()");
        model.addAttribute("meals", getAll());
        return "meals";
    }

    @GetMapping("/update")
    public String update(@RequestParam String id, Model model) {
        log.info("Controller - update(), id - {}", id);
        final Meal meal = get(getId(id));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/create")
    public String create(Model model) {
        log.info("Controller - create()");
        final Meal meal = new Meal();
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String id, Model model) {
        log.info("Controller - delete() id - {}", id);
        delete(getId(id));
        //   model.addAttribute("meals", getAll());
        //   return "meals";
        return "redirect:/meals";
    }

    @GetMapping("/filter")
    public String filter(@RequestParam String startDate, @RequestParam String endDate, @RequestParam String startTime, @RequestParam String endTime, Model model) {
        log.info("Controller - filter()");
        LocalDate lStartDate = parseLocalDate(startDate);
        LocalDate lEndDate = parseLocalDate(endDate);
        LocalTime lStartTime = parseLocalTime(startTime);
        LocalTime lEndTime = parseLocalTime(endTime);
        model.addAttribute("meals", getBetween(lStartDate, lStartTime, lEndDate, lEndTime));
        return "meals";
    }

    @PostMapping
    public String edit(@RequestParam String id
            , @RequestParam String dateTime
            , @RequestParam String description
            , @RequestParam String calories
            , Model model) {
        log.info("Controller - POST  - edit()");
        Meal meal = new Meal(
                LocalDateTime.parse(dateTime),
               description,
                Integer.parseInt(calories));

        if (StringUtils.isEmpty(id)) {
            create(meal);
        } else {
            update(meal, getId(id));
        }
        return "redirect:/meals";
    }

    private int getId(String id) {
        String paramId = Objects.requireNonNull(id);
        return Integer.parseInt(paramId);
    }

}
