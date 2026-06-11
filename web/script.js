/**
 * DAMmines · Landing Page — script.js
 * ─────────────────────────────────────────
 * Features:
 * 1. Dark / Light mode toggle (persisted in localStorage)
 * 2. Navbar scroll behaviour & MOBILE MENU TOGGLE
 * 3. Scroll-reveal animations (IntersectionObserver)
 * 4. Background pixel-grid flash effect
 * 5. FEATURE CARDS — keyboard focus stagger
 * 6. INTERACTIVE MINESWEEPER GAME & TIMER
 */

'use strict';

/* ─────────────────────────────────────────────────
   1. DARK MODE
   ───────────────────────────────────────────────── */
const darkToggle = document.getElementById('darkToggle');
const toggleIcon = darkToggle.querySelector('.toggle-icon');

const applyTheme = (isDark) => {
  document.documentElement.setAttribute('data-theme', isDark ? 'dark' : 'light');
  toggleIcon.textContent = isDark ? '☀️' : '🌙';
  if (darkToggle) darkToggle.setAttribute('aria-pressed', String(isDark));
};

const savedTheme = localStorage.getItem('dammines-theme');
const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
let isDark = savedTheme ? savedTheme === 'dark' : prefersDark;
applyTheme(isDark);

if (darkToggle) {
  darkToggle.addEventListener('click', () => {
    isDark = !isDark;
    applyTheme(isDark);
    localStorage.setItem('dammines-theme', isDark ? 'dark' : 'light');
  });
}

/* ─────────────────────────────────────────────────
   2. NAVBAR SCROLL & MOBILE MENU BEHAVIOUR
   ───────────────────────────────────────────────── */
const navbar = document.querySelector('.navbar');

// Efecto Scroll en Navbar
if (navbar) {
  window.addEventListener('scroll', () => {
    navbar.classList.toggle('scrolled', window.scrollY > 20);
  }, { passive: true });
}

// Toggle del menú desplegable en móvil (Envuelto para asegurar que el DOM cargue)
document.addEventListener('DOMContentLoaded', () => {
  const menuToggle = document.getElementById('menuToggle');
  const navLinks = document.getElementById('navLinks');

  if (menuToggle && navLinks) {
    menuToggle.addEventListener('click', () => {
      navLinks.classList.toggle('active');
      
      // Accesibilidad
      const isOpen = navLinks.classList.contains('active');
      menuToggle.setAttribute('aria-label', isOpen ? 'Cerrar menú' : 'Abrir menú');
    });

    // Cerrar el menú si se hace click en un enlace
    navLinks.querySelectorAll('a').forEach(link => {
      link.addEventListener('click', () => {
        navLinks.classList.remove('active');
      });
    });
  }
});

/* ─────────────────────────────────────────────────
   3. SCROLL-REVEAL (IntersectionObserver)
   ───────────────────────────────────────────────── */
const revealObserver = new IntersectionObserver((entries) => {
  entries.forEach((entry) => {
    if (!entry.isIntersecting) return;
    entry.target.classList.add('visible');

    const children = entry.target.querySelectorAll('.reveal');
    children.forEach((child, i) => {
      child.style.transitionDelay = `${i * 0.08}s`;
      child.classList.add('visible');
    });

    revealObserver.unobserve(entry.target);
  });
}, { threshold: 0.12 });

document.querySelectorAll('.reveal').forEach((el) => revealObserver.observe(el));

/* ─────────────────────────────────────────────────
   4. PIXEL BACKGROUND GRID
   ───────────────────────────────────────────────── */
const heroBg = document.querySelector('.hero-grid-bg');

const buildGrid = () => {
  if (!heroBg) return { cols: 0, rows: 0 };
  const W = window.innerWidth;
  const H = window.innerHeight;
  const cellSize = 48;
  const cols = Math.ceil(W / cellSize);
  const rows = Math.ceil(H / cellSize);

  heroBg.style.setProperty('--cols', cols);
  heroBg.style.setProperty('--rows', rows);
  heroBg.innerHTML = '';

  for (let i = 0; i < cols * rows; i++) {
    const div = document.createElement('div');
    div.className = 'grid-cell';
    heroBg.appendChild(div);
  }

  return { cols, rows };
};

let gridInfo = buildGrid();

const flashCell = () => {
  if (!heroBg) return;
  const cells = heroBg.querySelectorAll('.grid-cell');
  if (!cells.length) return;
  const idx = Math.floor(Math.random() * cells.length);
  const cell = cells[idx];
  cell.classList.add('flash');
  setTimeout(() => cell.classList.remove('flash'), 600);
};

if (heroBg) {
  setInterval(flashCell, 300);
}

let resizeTimer;
window.addEventListener('resize', () => {
  clearTimeout(resizeTimer);
  resizeTimer = setTimeout(() => { gridInfo = buildGrid(); }, 200);
}, { passive: true });

/* ─────────────────────────────────────────────────
   5. FEATURE CARDS — keyboard focus stagger
   ───────────────────────────────────────────────── */
document.querySelectorAll('.feature-card, .team-card').forEach((card) => {
  card.addEventListener('keydown', (e) => {
    if (e.key === 'Enter' || e.key === ' ') {
      e.preventDefault();
      card.classList.toggle('focused');
    }
  });
});

/* ─────────────────────────────────────────────────
   6. INTERACTIVE MINESWEEPER GAME & TIMER
   ───────────────────────────────────────────────── */
const BOARD_SIZE = 9;
const MINE_COUNT = 10;
const miniBoard = document.getElementById('miniBoard');
const flagCountEl = document.getElementById('flagCount');
const timerEl = document.getElementById('timer');

// Solo inicializar el juego si los elementos existen (para evitar errores en otras páginas)
if (miniBoard && flagCountEl && timerEl) {
  let board = [];
  let gameOver = false;
  let timerInterval = null;
  let seconds = 0;
  let firstClick = true;

  const padTwo = (n) => String(n).padStart(2, '0');

  const initGame = () => {
    gameOver = false;
    firstClick = true;
    seconds = 0;
    clearInterval(timerInterval);
    timerEl.textContent = "00:00";
    flagCountEl.textContent = MINE_COUNT;
    miniBoard.innerHTML = '';

    board = Array(BOARD_SIZE * BOARD_SIZE).fill(null).map(() => ({
      isMine: false, revealed: false, flagged: false, neighbor: 0
    }));

    board.forEach((_, i) => {
      const cell = document.createElement('div');
      cell.className = 'cell';
      cell.addEventListener('click', () => reveal(i));
      cell.addEventListener('contextmenu', (e) => { e.preventDefault(); flag(i); });
      miniBoard.appendChild(cell);
    });
  };

  const placeMines = (firstIndex) => {
    let m = 0;
    while (m < MINE_COUNT) {
      let p = Math.floor(Math.random() * board.length);
      if (!board[p].isMine && p !== firstIndex) {
        board[p].isMine = true;
        m++;
      }
    }

    for (let i = 0; i < board.length; i++) {
      if (board[i].isMine) continue;
      let count = 0;
      const row = Math.floor(i / BOARD_SIZE);
      const col = i % BOARD_SIZE;
      for (let r = -1; r <= 1; r++) {
        for (let c = -1; c <= 1; c++) {
          const nr = row + r, nc = col + c;
          if (nr >= 0 && nr < BOARD_SIZE && nc >= 0 && nc < BOARD_SIZE) {
            if (board[nr * BOARD_SIZE + nc].isMine) count++;
          }
        }
      }
      board[i].neighbor = count;
    }
  };

  const reveal = (i) => {
    if (gameOver || board[i].revealed || board[i].flagged) return;

    if (firstClick) {
      firstClick = false;
      placeMines(i);
      timerInterval = setInterval(() => {
        seconds++;
        timerEl.textContent = `${padTwo(Math.floor(seconds / 60))}:${padTwo(seconds % 60)}`;
      }, 1000);
    }

    board[i].revealed = true;
    const cells = miniBoard.children;
    cells[i].classList.add('open', 'reveal-anim');

    if (board[i].isMine) {
      cells[i].classList.add('mine');
      cells[i].textContent = '💣';
      endGame(false);
      return;
    }

    if (board[i].neighbor > 0) {
      cells[i].textContent = board[i].neighbor;
    } else {
      const row = Math.floor(i / BOARD_SIZE);
      const col = i % BOARD_SIZE;
      for (let r = -1; r <= 1; r++) {
        for (let c = -1; c <= 1; c++) {
          const nr = row + r, nc = col + c;
          if (nr >= 0 && nr < BOARD_SIZE && nc >= 0 && nc < BOARD_SIZE) {
            reveal(nr * BOARD_SIZE + nc);
          }
        }
      }
    }
    checkWin();
  };

  const flag = (i) => {
    if (gameOver || board[i].revealed) return;
    board[i].flagged = !board[i].flagged;
    const cells = miniBoard.children;
    
    if (board[i].flagged) {
      cells[i].classList.add('flag');
      cells[i].textContent = '🚩';
    } else {
      cells[i].classList.remove('flag');
      cells[i].textContent = '';
    }
    
    const flagsUsed = board.filter(c => c.flagged).length;
    flagCountEl.textContent = Math.max(0, MINE_COUNT - flagsUsed);
  };

  const endGame = (win) => {
    gameOver = true;
    clearInterval(timerInterval);
    
    board.forEach((c, idx) => {
      if (c.isMine && !c.flagged) {
        miniBoard.children[idx].classList.add('open', 'mine');
        miniBoard.children[idx].textContent = '💣';
      }
    });

    setTimeout(() => {
      if (win) alert(`¡Victoria! 🎉 Tiempo: ${timerEl.textContent}`);
      else alert('¡Derrota! 💥 Has pisado una mina.');
      initGame(); 
    }, 300);
  };

  const checkWin = () => {
    const revealedCount = board.filter(c => c.revealed).length;
    if (revealedCount === BOARD_SIZE * BOARD_SIZE - MINE_COUNT) endGame(true);
  };

  initGame();
}