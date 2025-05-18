package ru.rakhmanov.controller;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.rakhmanov.dto.response.PostFullDto;
import ru.rakhmanov.model.Comment;
import ru.rakhmanov.model.Tag;
import ru.rakhmanov.service.CommentService;
import ru.rakhmanov.service.FileStorageService;
import ru.rakhmanov.service.LikeService;
import ru.rakhmanov.service.PostService;
import ru.rakhmanov.service.TagService;

import java.util.List;

@Controller
@RequestMapping("/posts")
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final LikeService likeService;
    private final TagService tagService;
    private final FileStorageService fileStorageService;

    @GetMapping("/{id}")
    public String getPost(@PathVariable("id") Integer id, Model model) {
        PostFullDto post = postService.getPostById(id);
        List<Comment> comments = commentService.getCommentsByPostId(id);
        List<Tag> tags = tagService.getAllTags();

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("likesCount", post.getLikesCount());
        model.addAttribute("tags", tags);

        return "blog/post";
    }

    @PostMapping("/add")
    public String createPost(@RequestParam(name = "title") String title,
                             @RequestParam(name = "content") String content,
                             @RequestParam(name = "tags", required = false) List<Integer> tags,
                             @RequestParam(name = "image") MultipartFile image) {

            String imageUrl = fileStorageService.saveImage(image);
            postService.createPost(title, imageUrl, content, tags);
            return "redirect:/";
    }

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable(name = "id") Integer id,
                           @RequestParam(name = "title") String title,
                           @RequestParam(name = "content") String content,
                           @RequestParam(name = "tags", required = false) List<Integer> tags,
                           @RequestParam(name = "image") MultipartFile image) {

            String imageUrl = fileStorageService.saveImage(image);
            postService.editPost(id, title, imageUrl, content, tags);

            return "redirect:/posts/" + id;
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

}
