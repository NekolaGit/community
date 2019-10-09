package life.majiang.community.controller;

import life.majiang.community.mapper.PublishMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.model.Question;
import life.majiang.community.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Resource
    private PublishMapper publishMapper;

    @Resource
    private UserMapper userMapper;

    @GetMapping("/publish")
    public String publish() {
        return "publish";
    }

    @PostMapping("/publish")
    public String create(@RequestParam("title") String title,
                         @RequestParam("description") String description,
                         @RequestParam("tag") String tag,
                         Model model,
                         HttpServletRequest request) {
        // 数据回显
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);

        if (title == null || title.isEmpty()) {
            model.addAttribute("error","标题不能为空");
            return "publish";
        }

        if (description == null || description.isEmpty()) {
            model.addAttribute("error","内容不能为空");
            return "publish";
        }

        if (tag == null || tag.isEmpty()) {
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        // 登录用户数据显示
        User user = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    user = userMapper.findByToken(token);
                    if (user!=null) {
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }

        if (user == null) {
            model.addAttribute("error","用户登录失效");
            return "publish";
        }

        Question question = new Question();
        question.setTag(tag);
        question.setDescription(description);
        question.setTitle(title);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());

        publishMapper.create(question);

        return "redirect:/";
    }

}
