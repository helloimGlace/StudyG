package com.studg.servlet;

import com.studg.dao.UserDAO;
import com.studg.model.User;
import com.studg.service.SimpleRewardService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class OpenBoxServlet extends HttpServlet {
    private UserDAO userDAO;
    private SimpleRewardService rewardService;

    @Override
    public void init() throws ServletException {
        userDAO = com.studg.dao.DAOFactory.getUserDAO();
        rewardService = new SimpleRewardService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session==null || session.getAttribute("user")==null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String username = (String) session.getAttribute("user");
        User user = userDAO.findByUsername(username);
        if (user==null) { resp.sendError(HttpServletResponse.SC_UNAUTHORIZED); return; }

        // ensure session reflects latest DB values before decisions
        session.setAttribute("points", user.getPoints());
        session.setAttribute("plays", user.getPlays());

<<<<<<< HEAD
        // If plays <= 0, show preview and block further opens
        if (user.getPlays() <= 0) {
            sendPreviewJson(session, user, resp);
            return;
        }
        
        String idx = req.getParameter("index");
        int index = 0;
        try { index = Integer.parseInt(idx); } catch (Exception e) { index = 0; }
        
        // Check if this is the last play
        boolean isLastPlay = (user.getPlays() == 1);
        
        // Consume one play
        user.setPlays(user.getPlays()-1);
        
        // Open the box and get reward
        String reward = rewardService.openBox(index);
        String itemDisplay = null;
        boolean isPlayReward = false;
        
        if (reward.startsWith("points:")) {
            int pts = Integer.parseInt(reward.split(":")[1]);
            user.setPoints(user.getPoints()+pts);
        } else if (reward.startsWith("play:")) {
            int pl = Integer.parseInt(reward.split(":")[1]);
            user.setPlays(user.getPlays()+pl);
            isPlayReward = true;
=======
        if (checkPlays(session, resp, true)) return;
        String idx = req.getParameter("index");
        int index = 0;
        try { index = Integer.parseInt(idx); } catch (Exception e) { index = 0; }

        if (user.getPlays()<=0) {
            // Preview-only behavior: reveal all boxes for the user to see without changing stats.
            int total = rewardService.getBoxCount();
            Random rnd = new Random();
            List<String> base = new ArrayList<>();
            for (int i=0;i<total;i++) {
                base.add(rewardService.openBoxNoJackpot(i));
            }
            // get previously picked boxes from session and treat them as excluded
            @SuppressWarnings("unchecked")
            Set<Integer> previouslyPicked = (Set<Integer>) session.getAttribute("pickedBoxes");
            if (previouslyPicked == null) previouslyPicked = new HashSet<>();
            // mark excluded
            for (Integer ex : previouslyPicked) {
                if (ex >=0 && ex < total) base.set(ex, "excluded");
            }
            // ensure between 1 and 2 jackpots (display only)
            int jackpots = rnd.nextInt(2) + 1; // 1 or 2
            Set<Integer> picks = new HashSet<>();
            // build list of available indices (not previously picked)
            List<Integer> available = new ArrayList<>();
            for (int i=0;i<total;i++) if (!previouslyPicked.contains(i)) available.add(i);
            if (!available.isEmpty()) {
                int toPick = Math.min(jackpots, available.size());
                while (picks.size() < toPick) {
                    int cand = available.get(rnd.nextInt(available.size()));
                    picks.add(cand);
                }
            }
            for (int p: picks) base.set(p, "play:100");

            List<String> itemDisplays = new ArrayList<>();
            for (int i=0;i<total;i++) {
                String reward = base.get(i);
                String itemDisplay = null;
                if ("excluded".equals(reward)) {
                    // leave itemDisplay null
                } else if (reward.startsWith("item:")) {
                    String key = reward.split(":")[1];
                    switch (key) {
                        case "rare_profile_sticker": itemDisplay = "⭐ Rare Sticker"; break;
                        case "sticker_rare": itemDisplay = "⭐ Rare Sticker"; break;
                        case "play_ticket": itemDisplay = "🎟️ Play Ticket"; break;
                        default: itemDisplay = key; break;
                    }
                }
                itemDisplays.add(itemDisplay);
            }

            // build JSON (no user changes)
            StringBuilder sb = new StringBuilder();
            sb.append('{');
            sb.append("\"multi\":true,");
            sb.append("\"results\":[");
            for (int i=0;i<base.size();i++) {
                String rwd = base.get(i).replace("\"","\\\"");
                sb.append('{');
                sb.append("\"reward\":\"").append(rwd).append('\"');
                String idisp = itemDisplays.get(i);
                if (idisp!=null) {
                    sb.append(",\"itemDisplay\":\"").append(idisp.replace("\"","\\\"")).append('\"');
                }
                sb.append('}');
                if (i < base.size()-1) sb.append(',');
            }
            sb.append("],\"points\":").append(user.getPoints()).append(",\"plays\":").append(user.getPlays());
            sb.append('}');
            resp.setContentType("application/json");
            resp.getWriter().write(sb.toString());
            return;
        }
        // normal single-box open consumes one play
        user.setPlays(user.getPlays()-1);
        String reward = rewardService.openBox(index);
        String itemDisplay = null;
        if (reward.startsWith("points:")) {
            int pts = Integer.parseInt(reward.split(":")[1]);
            user.setPoints(user.getPoints()+pts);
            session.setAttribute("points", user.getPoints());
        } else if (reward.startsWith("play:")) {
            int pl = Integer.parseInt(reward.split(":")[1]);
            user.setPlays(user.getPlays()+pl);
            session.setAttribute("plays", user.getPlays());
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
        } else if (reward.startsWith("item:")) {
            String key = reward.split(":")[1];
            user.addItem(key);
            // small display mapping (emoji + name)
            switch (key) {
<<<<<<< HEAD
                case "rare_profile_sticker": itemDisplay = "Rare Sticker"; break;
                case "sticker_rare": itemDisplay = "Rare Sticker"; break;
                case "play_ticket": itemDisplay = "Play Ticket"; break;
                default: itemDisplay = key; break;
            }
        }
        
=======
                case "rare_profile_sticker": itemDisplay = "⭐ Rare Sticker"; break;
                case "sticker_rare": itemDisplay = "⭐ Rare Sticker"; break;
                case "play_ticket": itemDisplay = "🎟️ Play Ticket"; break;
                default: itemDisplay = key; break;
            }
        }
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
        // record that this index has been picked by the user in this session
        @SuppressWarnings("unchecked")
        Set<Integer> picked = (Set<Integer>) session.getAttribute("pickedBoxes");
        if (picked == null) picked = new HashSet<>();
        picked.add(index);
        session.setAttribute("pickedBoxes", picked);
<<<<<<< HEAD
        
        // Save to database
        userDAO.save(user);
        
        // Always sync session with latest database values
        session.setAttribute("points", user.getPoints());
        session.setAttribute("plays", user.getPlays());
        
        resp.setContentType("application/json");
        
        // If this was the last play and reward is NOT plays, show multi-reveal
        if (isLastPlay && !isPlayReward) {
            user.setPlays(0);
            userDAO.save(user);
            session.setAttribute("plays", 0);
            String multiReveal = buildPreviewJson(session, user, index, reward, itemDisplay);
            resp.getWriter().write(multiReveal);
            return;
        }
        
        // If plays reach 0 (and wasn't a last-play non-play-reward scenario), return multi-reveal
        if (user.getPlays() <= 0) {
            String multiReveal = buildPreviewJson(session, user, -1, null, null);
            resp.getWriter().write(multiReveal);
            return;
        }
        
        // Normal response: single-box reveal
=======
        userDAO.save(user);
        session.setAttribute("plays", user.getPlays());
        resp.setContentType("application/json");
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
        String json;
        if (itemDisplay!=null) {
            json = String.format("{\"reward\":\"%s\",\"itemDisplay\":\"%s\",\"points\":%d,\"plays\":%d}", reward.replace("\"","\\\""), itemDisplay.replace("\"","\\\""), user.getPoints(), user.getPlays());
        } else {
            json = String.format("{\"reward\":\"%s\",\"points\":%d,\"plays\":%d}", reward.replace("\"","\\\""), user.getPoints(), user.getPlays());
        }
        resp.getWriter().write(json);
<<<<<<< HEAD
    }

    private void sendPreviewJson(HttpSession session, User user, HttpServletResponse resp) throws IOException {
        String preview = buildPreviewJson(session, user);
        session.setAttribute("previewJson", preview);
        resp.setContentType("application/json");
        resp.getWriter().write(preview);
    }

    private String buildPreviewJson(HttpSession session, User user, int currentIndex, String currentReward, String currentItemDisplay) {
=======
        // after handling a post, update preview session data (no direct response)
        checkPlays(session, resp, false);
    }

    // returns true if it already wrote a preview response
    private boolean checkPlays(HttpSession session, HttpServletResponse resp, boolean writeResponse) throws IOException {
        String username = (String) session.getAttribute("user");
        if (username == null) return false;
        User fresh = userDAO.findByUsername(username);
        if (fresh == null) return false;
        // sync session
        session.setAttribute("points", fresh.getPoints());
        session.setAttribute("plays", fresh.getPlays());
        if (fresh.getPlays() > 0) return false;

        // build preview JSON and optionally write it
        String preview = buildPreviewJson(session, fresh);
        session.setAttribute("previewJson", preview);
        if (writeResponse) {
            resp.setContentType("application/json");
            resp.getWriter().write(preview);
            return true;
        }
        return false;
    }

    private String buildPreviewJson(HttpSession session, User user) {
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
        int total = rewardService.getBoxCount();
        Random rnd = new Random();
        List<String> base = new ArrayList<>();
        for (int i=0;i<total;i++) base.add(rewardService.openBoxNoJackpot(i));
        @SuppressWarnings("unchecked")
        Set<Integer> previouslyPicked = (Set<Integer>) session.getAttribute("pickedBoxes");
        if (previouslyPicked == null) previouslyPicked = new HashSet<>();
        for (Integer ex : previouslyPicked) if (ex>=0 && ex<total) base.set(ex, "excluded");
<<<<<<< HEAD
        
        // If we have a current opened box, use its actual reward instead of regenerated one
        if (currentIndex >= 0 && currentIndex < total) {
            base.set(currentIndex, currentReward);
        }
        // Set random preview Jackpot value
        int randomJackpots = rnd.nextInt(5);
        int jackpots = randomJackpots <= 3 ? rnd.nextInt(2) + 1 : 0;
        Set<Integer> picks = new HashSet<>();
        List<Integer> available = new ArrayList<>();
        for (int i=0;i<total;i++) if (!previouslyPicked.contains(i) && i != currentIndex) available.add(i);
=======
        int jackpots = rnd.nextInt(2) + 1;
        Set<Integer> picks = new HashSet<>();
        List<Integer> available = new ArrayList<>();
        for (int i=0;i<total;i++) if (!previouslyPicked.contains(i)) available.add(i);
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
        if (!available.isEmpty()) {
            int toPick = Math.min(jackpots, available.size());
            while (picks.size() < toPick) {
                int cand = available.get(rnd.nextInt(available.size()));
                picks.add(cand);
            }
        }
        for (int p: picks) base.set(p, "play:100");

        List<String> itemDisplays = new ArrayList<>();
        for (int i=0;i<total;i++) {
            String reward = base.get(i);
            String itemDisplay = null;
            if ("excluded".equals(reward)) {
                // leave itemDisplay null
<<<<<<< HEAD
            } else if (i == currentIndex && currentItemDisplay != null) {
                // Use the provided itemDisplay for current box
                itemDisplay = currentItemDisplay;
            } else if (reward.startsWith("item:")) {
                String key = reward.split(":")[1];
                switch (key) {
                    case "rare_profile_sticker": itemDisplay = "Rare Sticker"; break;
                    case "sticker_rare": itemDisplay = "Rare Sticker"; break;
                    case "play_ticket": itemDisplay = "Play Ticket"; break;
=======
            } else if (reward.startsWith("item:")) {
                String key = reward.split(":")[1];
                switch (key) {
                    case "rare_profile_sticker": itemDisplay = "⭐ Rare Sticker"; break;
                    case "sticker_rare": itemDisplay = "⭐ Rare Sticker"; break;
                    case "play_ticket": itemDisplay = "🎟️ Play Ticket"; break;
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
                    default: itemDisplay = key; break;
                }
            }
            itemDisplays.add(itemDisplay);
        }
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("\"multi\":true,");
        sb.append("\"results\":[");
        for (int i=0;i<base.size();i++) {
            String rwd = base.get(i).replace("\"","\\\"");
            sb.append('{');
            sb.append("\"reward\":\"").append(rwd).append('\"');
            String idisp = itemDisplays.get(i);
            if (idisp!=null) {
                sb.append(",\"itemDisplay\":\"").append(idisp.replace("\"","\\\"")).append('\"');
            }
            sb.append('}');
            if (i < base.size()-1) sb.append(',');
        }
        sb.append("],\"points\":").append(user.getPoints()).append(",\"plays\":").append(user.getPlays());
        sb.append('}');
        return sb.toString();
    }
<<<<<<< HEAD

    // Overload for backward compatibility when no current box info
    private String buildPreviewJson(HttpSession session, User user) {
        return buildPreviewJson(session, user, -1, null, null);
    }
=======
>>>>>>> 1139f5830d202aa8582165450a4d93ed08554eb5
}
