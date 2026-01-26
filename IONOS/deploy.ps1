# ============================================================
# 🛡️ SCRIPT DI DEPLOY SICURO - SOLO PUSH (NO PULL)
# ============================================================
# Questo script esegue SOLO push verso GitHub.
# NON esegue mai pull per proteggere i file locali.
# ============================================================

param(
    [string]$CommitMessage = "Deploy update $(Get-Date -Format 'yyyy-MM-dd HH:mm')"
)

Write-Host ""
Write-Host "🛡️ =========================================" -ForegroundColor Cyan
Write-Host "   DEPLOY SICURO - SCAM-RADAR.COM" -ForegroundColor Cyan
Write-Host "   Push unidirezionale verso GitHub" -ForegroundColor Cyan
Write-Host "🛡️ =========================================" -ForegroundColor Cyan
Write-Host ""

# Verifica che siamo nella cartella corretta
$currentPath = Get-Location
if (-not (Test-Path ".\.git")) {
    Write-Host "❌ Errore: Non sei in un repository Git!" -ForegroundColor Red
    Write-Host "   Esegui prima: git init" -ForegroundColor Yellow
    exit 1
}

# Mostra stato attuale
Write-Host "📁 Cartella: $currentPath" -ForegroundColor Gray
Write-Host "📝 Messaggio commit: $CommitMessage" -ForegroundColor Gray
Write-Host ""

# Conferma prima di procedere
Write-Host "⚠️  ATTENZIONE: Stai per eseguire un PUSH verso GitHub." -ForegroundColor Yellow
Write-Host "   Questo attiverà il deploy automatico su IONOS." -ForegroundColor Yellow
Write-Host ""
$confirm = Read-Host "Vuoi procedere? (s/n)"

if ($confirm -ne "s" -and $confirm -ne "S") {
    Write-Host "❌ Operazione annullata." -ForegroundColor Red
    exit 0
}

Write-Host ""
Write-Host "📤 Aggiunta file..." -ForegroundColor Green
git add .

Write-Host "📝 Creazione commit..." -ForegroundColor Green
git commit -m $CommitMessage

Write-Host "🚀 Push verso GitHub (NO PULL!)..." -ForegroundColor Green
git push origin main --force-with-lease

Write-Host ""
Write-Host "✅ =========================================" -ForegroundColor Green
Write-Host "   DEPLOY COMPLETATO!" -ForegroundColor Green
Write-Host "   GitHub Actions ora deploya su IONOS" -ForegroundColor Green
Write-Host "✅ =========================================" -ForegroundColor Green
Write-Host ""
Write-Host "🔗 Controlla: https://github.com/TUO_USERNAME/scam-radar/actions" -ForegroundColor Cyan
