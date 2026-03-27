<%@ page session="true" %>
<%
    String user = (String) session.getAttribute("user");
    if (user == null) { response.sendRedirect("login.jsp"); return; }
    Integer plays = (Integer) session.getAttribute("plays"); if (plays==null) plays=0;
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Mystery Boxes</title>
    <link rel="stylesheet" href="css/style.css" />
    <style>
        /* Override cho grid riêng c?a mystery */
        .mystery-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 1rem;
            max-width: 700px;
            margin: 2rem auto;
        }
        .box {
            background: #fff;
            border-radius: 20px;
            padding: 1.5rem 0.5rem;
            text-align: center;
            cursor: pointer;
            font-weight: 600;
            transition: all 0.2s;
            border: 1px solid #fed7aa;
            box-shadow: 0 2px 4px rgba(0,0,0,0.02);
        }
        .box:hover {
            transform: scale(0.98);
            box-shadow: 0 8px 20px rgba(0,0,0,0.1);
        }
        /* Reward color classes (cam) */
        .box.jackpot { background: linear-gradient(135deg, #c2410c, #9a3412); color: #fff; border: none; }
        .box.sticker { background: linear-gradient(135deg, #ffb347, #f97316); color: #fff; border: none; }
        .box.play2 { background: linear-gradient(135deg, #f97316, #ea580c); color: #fff; border: none; }
        .box.play1 { background: linear-gradient(135deg, #fb923c, #f97316); color: #fff; border: none; }
        .box.points50 { background: linear-gradient(135deg, #fdba74, #f97316); color: #2d2a24; border: none; }
        .box.none { background: #fff7ed; color: #c2410c; border: 1px solid #fed7aa; }

        .box.jackpotprev, .box.stickerprev, .box.play2prev, .box.play1prev, .box.points50prev {
            opacity: 0.7;
            filter: grayscale(0.2);
        }
    </style>
    <script>
        async function openBox(el, idx) {
            if (el.dataset.opened==='true') return;
            try {
                el.textContent = 'Opening...';
                const body = 'action=OpenBox&index=' + encodeURIComponent(idx);
                const res = await fetch('MainController',{method:'POST', headers:{'Content-Type':'application/x-www-form-urlencoded'}, body: body});
                if (!res.ok) { el.textContent='Error'; return; }
                const j = await res.json();
                if (j.error) { el.textContent = j.error; return; }
                // multi reveal (when plays == 0)
                if (j.multi && Array.isArray(j.results)) {
                    const boxes = document.querySelectorAll('.box');
                    for (let i = 0; i < j.results.length && i < boxes.length; i++) {
                        const b = boxes[i];
                        const r = j.results[i];
                        // skip previously picked / excluded boxes
                        if (r.reward === 'excluded') continue;
                        b.classList.remove('jackpotprev', 'stickerprev', 'play2prev', 'play1prev', 'points50prev', 'jackpot', 'sticker', 'play2', 'play1', 'points50', 'none');
                        b.classList.add('opened');
                        b.dataset.opened = 'true';
                        const suffix = (i === idx) ? '' : 'prev'; 

                        if (r.reward && r.reward.startsWith('play:100')) {
                            b.classList.add('jackpot' + suffix);
                            b.textContent = 'JACKPOT (100 plays)';
                        } else if (r.reward && r.reward.startsWith('item:')) {
                            b.classList.add('sticker' + suffix);
                            b.textContent = r.itemDisplay ? r.itemDisplay : r.reward;
                        } else if (r.reward && r.reward.startsWith('play:2')) {
                            b.classList.add('play2' + suffix);
                            b.textContent = 'Play x2';
                        } else if (r.reward && r.reward.startsWith('play:1')) {
                            b.classList.add('play1' + suffix);
                            b.textContent = 'Play x1';
                        } else if (r.reward && r.reward.startsWith('points:')) {
                            b.classList.add('points50' + suffix);
                            b.textContent = '50 pts';
                        } else {
                            b.classList.add('none');
                            b.textContent = r.reward;
                        }
                    }
                    // Update stats display
                    document.getElementById('plays').textContent = j.plays;
                    document.getElementById('points').textContent = j.points;
                    return;
                }
                // single reveal - apply color class and friendly text
                el.classList.remove('jackpotprev','stickerprev','play2prev','play1prev','points50prev','jackpot','sticker','play2','play1','points50','none');
                el.classList.add('opened'); el.dataset.opened='true';
                if (j.reward && j.reward.startsWith('play:100')) {
                    el.classList.add('jackpot'); el.textContent = 'JACKPOT (100 plays)';
                } else if (j.reward && j.reward.startsWith('item:')) {
                    el.classList.add('sticker'); el.textContent = j.itemDisplay ? j.itemDisplay : j.reward;
                } else if (j.reward && j.reward.startsWith('play:2')) {
                    el.classList.add('play2'); el.textContent = 'Play x2';
                } else if (j.reward && j.reward.startsWith('play:1')) {
                    el.classList.add('play1'); el.textContent = 'Play x1';
                } else if (j.reward && j.reward.startsWith('points:')) {
                    el.classList.add('points50'); el.textContent = '50 pts';
                } else {
                    el.classList.add('none'); el.textContent = j.reward;
                }
                document.getElementById('plays').textContent = j.plays;
                document.getElementById('points').textContent = j.points;
            } catch(e){ el.textContent='Err'; }
        }
    </script>
    <script>
        // auto-preview when plays == 0
        window.addEventListener('DOMContentLoaded', function() {
            try {
                var playsEl = document.getElementById('plays');
                if (playsEl && parseInt(playsEl.textContent) === 0) {
                    // trigger preview request (no index required)
                    const body = 'action=OpenBox&index=0';
                    fetch('MainController', {method:'POST', headers:{'Content-Type':'application/x-www-form-urlencoded'}, body: body})
                        .then(r => r.json())
                        .then(j => {
                            if (j && j.multi && Array.isArray(j.results)) {
                                // reuse existing multi handling by calling openBox handler logic
                                const boxes = document.querySelectorAll('.box');
                                for (let i = 0; i < j.results.length && i < boxes.length; i++) {
                                    const b = boxes[i];
                                    const r = j.results[i];
                                    // skip excluded boxes
                                    if (r.reward === 'excluded') continue;
                                    b.classList.remove('jackpot','sticker','play2','play1','points50','none', 'jackpotprev','stickerprev','play2prev','play1prev','points50prev');
                                    b.classList.add('opened'); b.dataset.opened='true';
                                    if (r.reward && r.reward.startsWith('play:100')) {
                                        b.classList.add('jackpot'); b.textContent = 'JACKPOT (100 plays)';
                                    } else if (r.reward && r.reward.startsWith('item:')) {
                                        b.classList.add('sticker'); b.textContent = r.itemDisplay ? r.itemDisplay : r.reward;
                                    } else if (r.reward && r.reward.startsWith('play:2')) {
                                        b.classList.add('play2'); b.textContent = 'Play x2';
                                    } else if (r.reward && r.reward.startsWith('play:1')) {
                                        b.classList.add('play1'); b.textContent = 'Play x1';
                                    } else if (r.reward && r.reward.startsWith('points:')) {
                                        b.classList.add('points50'); b.textContent = '50 pts';
                                    } else {
                                        b.classList.add('none'); b.textContent = r.reward;
                                    }
                                }
                                // preview only: do not change displayed stats
                            }
                        }).catch(()=>{});
                }
            } catch(e) {}
        });
    </script>
    <script>
        // clear picked boxes when leaving the mystery page so boxes reset
        window.addEventListener('beforeunload', function() {
            try {
                navigator.sendBeacon('MainController?action=ClearPicked');
            } catch(e) {
                // best-effort
                fetch('MainController?action=ClearPicked', {method:'POST', keepalive:true}).catch(()=>{});
            }
        });
    </script>
</head>
<body>
    <div class="topbar">
        <div>Player: <b><%= user %></b></div>
        <div>Points: <span id="points"><%= session.getAttribute("points") != null ? session.getAttribute("points") : 0 %></span> | Plays: <span id="plays"><%= plays %></span></div>
    </div>
    <div class="grid">
        <% for (int i=0;i<16;i++) { %>
            <div class="box" onclick="openBox(this,<%=i%>)">Box <%=i+1%></div>
        <% } %>
    </div>
    <p style="text-align:center"><a href="MainController?action=Game">Back to game</a></p>
</body>
</html>
