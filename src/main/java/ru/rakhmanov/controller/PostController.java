package ru.rakhmanov.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.model.Comment;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.service.CommentService;
import ru.rakhmanov.service.LikeService;
import ru.rakhmanov.service.PostService;
import ru.rakhmanov.service.TagService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private static final String LINK_PATH = "/uploads/";
    private static final String UPLOAD_DIR = System.getProperty("catalina.base") + "/webapps" + LINK_PATH;

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final TagService tagService;

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Integer id, Model model) {
        PostFullDto post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPostId(id);
        Integer likesCount = likeService.getLikesCountByPostId(id);
        List<Tag> tags = tagService.getAllTags();

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("likesCount", likesCount);
        model.addAttribute("tags", tags);

        return "blog/post";
    }

    @PostMapping("/add")
    public String createPost(@RequestParam(name = "title") String title,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "tags", required = false) List<Integer> tags,
                             @RequestParam(name = "image") MultipartFile image) {

        try {
            String imageUrl = saveImage(image);
            postService.createPost(title, imageUrl, content, tags);
            return "redirect:/";
        } catch (IOException e) {

            return "redirect:/";
        }
    }

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable(name = "id") Integer id,
                           @RequestParam(name = "title") String title,
                           @RequestParam(name = "content") String content,
                           @RequestParam(name = "tags", required = false) List<Integer> tags,
                           @RequestParam(name = "image") MultipartFile image) {

        try {
            String imageUrl = saveImage(image);
            postService.editPost(id, title, imageUrl, content, tags);

            return "redirect:/posts/" + id;
        } catch (IOException e) {
            return "redirect:/posts/" + id;
        }
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable("id") Integer id, Model model) {
        postService.deletePostById(id);

        return "redirect:/";
    }

    @PostMapping("/{id}/comments/add")
    public String addComment(@PathVariable(name = "id") Integer id,
                             @RequestParam("commentText") String commentText) {

        commentService.addComment(id, commentText);

        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/like")
    public String likePost(@PathVariable(name = "id") Integer id) {
        postService.likePost(id);

        return "redirect:/posts/" + id;
    }

    private String saveImage(MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        Files.write(filePath, image.getBytes());

        return LINK_PATH + fileName;
    }

}
