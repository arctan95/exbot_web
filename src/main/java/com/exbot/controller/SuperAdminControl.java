package com.exbot.controller;

import com.exbot.service.*;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @author: tan
 * @Date: 2019/1/25 16:14
 * Describe: 超级管理页面
 */
@RestController
public class SuperAdminControl {

    @Autowired
    FeedBackService feedBackService;
    @Autowired
    VisitorService visitorService;
    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;


    /**
     * 分页获得所有反馈信息
     * @param rows 一页大小
     * @param pageNum 当前页
     */
    @GetMapping("/getAllFeedback")
    public JSONObject getAllFeedback(@RequestParam("rows") String rows,
                                     @RequestParam("pageNum") String pageNum){
        return feedBackService.getAllFeedback(Integer.parseInt(rows),Integer.parseInt(pageNum));
    }

    /**
     * 获得统计信息
     * @return
     */
    @GetMapping("/getStatisticsInfo")
    public JSONObject getStatisticsInfo(){
        JSONObject returnJson = new JSONObject();
        long num = visitorService.getAllVisitor();
        returnJson.put("allVisitor", num);
        returnJson.put("allUser", userService.countUserNum());
        returnJson.put("yesterdayVisitor", num);
        returnJson.put("articleNum", articleService.countArticle());
        return returnJson;
    }

    /**
     * 获得文章管理
     * @return
     */
    @GetMapping("/getArticleManagement")
    public JSONObject getArticleManagement(@AuthenticationPrincipal Principal principal,
                                           @RequestParam("rows") String rows,
                                           @RequestParam("pageNum") String pageNum){
        String username = null;
        JSONObject returnJson = new JSONObject();
        try {
            username = principal.getName();
        } catch (NullPointerException e){
            returnJson.put("status",403);
            return  returnJson;
        }
        return articleService.getArticleManagement(Integer.parseInt(rows), Integer.parseInt(pageNum));
    }

    /**
     * 删除文章
     * @param id 文章id
     * @return 1--删除成功   0--删除失败
     */
    @GetMapping("/deleteArticle")
    @PreAuthorize("hasRole('ROLE_SUPERADMIN')")
    public int deleteArticle(@RequestParam("id") String id){
        if("".equals(id) || id == null){
            return 0;
        }
        return articleService.deleteArticle(Long.parseLong(id));
    }
}
