# 📊 Analisi Tecnica: Root vs APP - PARTE 4

## Responsive Design & JavaScript

### Media Queries Strategy

**Entrambe usano**: Grid responsive + Flexbox adaptive

```css
/* Desktop (default) */
.container { max-width: 1400px; }

/* Grid auto-responsive */
.countries-grid { 
    display: grid; 
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr)); 
}

/* Mobile: handled via flex-wrap e container width */
```

**Nessuna differenza** in media queries tra root e APP.

---

## ⚙️ JavaScript Event Handling

### Medusa CTA Interaction (Identico in Entrambe)

```javascript
const medusaCta = document.getElementById('caravaggio-medusa-link');
const reducedMotionQuery = window.matchMedia('(prefers-reduced-motion: reduce)');

medusaCta.addEventListener('mouseenter', animateSnakeSerpent);
medusaCta.addEventListener('mousemove', updateSnakeMotion);
medusaCta.addEventListener('mouseleave', resetMedusaMotion);
medusaCta.addEventListener('focus', resetMedusaMotion, true);
```

**Accessibility features**:
- ✅ prefers-reduced-motion support
- ✅ Keyboard navigation
- ✅ Safari compatibility
- ✅ Event cleanup on pagehide

---

## 🎯 Responsive Layout Differences

| Elemento | Root | APP | Impatto |
|----------|------|-----|---------|
| **Language selector** | Grid 980px | Flex centered | APP mobile-first |
| **Flag orbit banner** | 14px bottom | 6px bottom | APP compatto |
| **Container max-width** | 1400px | 1400px | Identico |
| **Card min-width** | 350px | 350px | Identico |

**Principale differenza**: Header spacing su mobile (APP 6px vs Root 14px)

---

## 📝 Conclusione Parte 4

✅ JavaScript evento handling **identico**  
✅ Responsive strategy identica (grid + flex)  
✅ Unica differenza: Header spacing visual  
✅ Medusa CTA fully responsive in entrambe
