package edu.cscc.degrees.ui;

import edu.cscc.degrees.data.MenuCategoryRepository;
import edu.cscc.degrees.domain.MenuCategory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/categories")
public class WebMenuCategoryController {
  private final MenuCategoryRepository menuCategoryRepository;

  public WebMenuCategoryController(
    MenuCategoryRepository menuCategoryRepository) {
    this.menuCategoryRepository = menuCategoryRepository;
  }

  // @GetMapping
  // public String getAllMenuCategories (Model model) {
  @GetMapping
  public String getAllMenuCategories (Model model) {
    model.addAttribute("categoryList", menuCategoryRepository.findAll());
    return "categories";
  }

  // @GetMapping("{id}")
  @GetMapping("{id}")
  public String getMenuCategoryById (@PathVariable Long id, Model model) {
    Optional<MenuCategory> result = menuCategoryRepository.findById(id);
    if (result.isPresent()) {
      model.addAttribute("menuCategory", result.get());
      return "edit_category";
    } else {
      return "error/404";
    }
  }

  @GetMapping("/new")
  public String newMenuCategory (Model model) {
    model.addAttribute("menuCategory", new MenuCategory());
    return "edit_category";
  }

  @PostMapping
  public String saveMenuCategory (@Valid MenuCategory newMenuCategory,
  Errors errors, RedirectAttributes redirectAttributes) {
    if (errors.hasErrors()) {
      return "edit_category";
    }
    menuCategoryRepository.save(newMenuCategory);
    redirectAttributes.addFlashAttribute("message",
      String.format("Category '%s' saved", newMenuCategory.getCategoryTitle()));
    return "redirect:/categories";
  }

  @GetMapping("/delete/{id}")
  public String deleteMenuCategoryById (@PathVariable Long id, RedirectAttributes redirectAttributes) {
    Optional<MenuCategory> result = menuCategoryRepository.findById(id);
    if (result.isPresent()) {
      menuCategoryRepository.delete(result.get());
      redirectAttributes.addFlashAttribute("message",
              String.format("Category %s deleted", id));
    } else {
      redirectAttributes.addFlashAttribute("error", "True");
      redirectAttributes.addFlashAttribute("message", String.format("Category %d not found", id));
    }
    return "redirect:/categories";
  }

}
