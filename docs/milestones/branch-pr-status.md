# Branch & PR Status — RasterOneLab LIS

> **Updated:** 2026-03-20
> **Purpose:** Explain why several `copilot/*` branches exist without merged PRs and define the remediation plan.

---

## Root Cause Summary

Three overlapping factors caused branches to accumulate without PRs:

| # | Root Cause | Impact |
|---|-----------|--------|
| 1 | **No `.github/` infrastructure** — no CI workflow, no PR template, no branch-protection rules requiring PRs | Any commit could be pushed to `main` directly; agents had no guardrails |
| 2 | **Parallel agent sessions** — multiple Copilot agent runs attempted the same phase concurrently, each on its own branch | Duplicate branches (e.g. `complete-phase-2-tasks`, `complete-phase-2-task-list`, `lis-023-complete-phase-2-tasks`) |
| 3 | **No stale-branch automation** — old branches are never pruned | 22 `copilot/*` branches accumulated; developers lose track of what is pending vs abandoned |

---

## Branch Inventory

The table below covers every `copilot/*` branch that existed at the time of this review (2026-03-20).

| Branch | Has Open PR? | Recommendation | Notes |
|--------|-------------|----------------|-------|
| `copilot/setup-complete-project-structure` | ❌ PR merged (PR #1 — old) | ✅ Delete branch | Work fully merged; branch is stale |
| `copilot/complete-phase-1-task-list` | ❌ No PR | 🗑 Delete | Phase 1 is 100% done; work is in `main` via PRs #5–#7 |
| `copilot/add-seed-data-departments` | ❌ No PR | 🗑 Delete | Seed data shipped in PR #16 (R__001–R__012) |
| `copilot/remove-lombok-and-update-files` | ❌ No PR | 🗑 Delete | Lombok removal merged in PR #11 |
| `copilot/check-development-milestone-phases` | ❌ No PR | 🗑 Delete | Analysis/docs branch; content merged via PRs #13, #18, #23 |
| `copilot/check-project-phases-completion` | ❌ No PR | 🗑 Delete | Duplicate of above |
| `copilot/create-milestone-from-md-files` | ❌ No PR | 🗑 Delete | Milestone docs are in `docs/milestones/`; no unique changes |
| `copilot/review-lab-information-system` | ❌ No PR | 🗑 Delete | Review branch; content is now in PROJECT-STATUS.md |
| `copilot/review-phase-1-2-3` | ❌ No PR | 🗑 Delete | Content merged via PR #13 |
| `copilot/review-phase-progress` | ❌ No PR | 🗑 Delete | Superseded by PR #23 (PROJECT-STATUS.md update) |
| `copilot/complete-all-phases-lis` | ❌ No PR | 🗑 Delete | Over-ambitious single-branch attempt; work split across PRs #8–#22 |
| `copilot/complete-all-phases-of-lis` | ❌ No PR | 🗑 Delete | Near-duplicate of above |
| `copilot/complete-rasteronelab-lis-phases` | ❌ No PR | 🗑 Delete | Third duplicate of the same intent |
| `copilot/start-development-phase-by-phase` | ❌ No PR | 🗑 Delete | Superseded by phased PRs #5 → #22 |
| `copilot/setup-complete-project-structure` | ❌ (merged) | ✅ Already handled | |
| `copilot/complete-phase-2-task-list` | ❌ No PR | 🗑 Delete | Phase 2 is ~97% done via PRs #8, #9, #15, #16 |
| `copilot/lis-023-complete-phase-2-tasks` | ❌ No PR | 🗑 Delete | LIS-023 (Price Catalog) is ✅ Done in main |
| `copilot/complete-phase-2-tasks` | ⚠️ **Open PR #1** | 🔄 Close PR + delete branch | PR is stale — its work was merged via PRs #8, #9, #15, #16; has conflicts with `main` |
| `copilot/complete-phase-3-tasks-again` | ❌ No PR | 🗑 Delete | Retry of above; same situation |
| `copilot/lis-029-complete-pending-tasks` | ❌ No PR | 🗑 Delete | LIS-029 (Branch Config screen) is ✅ Done |
| `copilot/complete-phase-eight-issues` | ❌ No PR | 🗑 Delete | Phase 8 is ⬜ Not Started — branch has no meaningful commits |
| `copilot/update-workflow-billing-first` | ❌ No PR | 🗑 Delete | CLAUDE.md update merged in PR #4 |

---

## The One Genuinely Open PR

### PR #1 — `copilot/complete-phase-2-tasks` → `main`

**Status:** Open, no reviews, no CI checks, likely has conflicts.

**Why it should be closed:**
- Phase 2 backend was fully merged through PRs #8 (`bb33b380`), #9 (`2d92c37e`), #15 (`5ddc3ccf`), and #16 (`eea1bc33`).
- `main` is 15+ commits ahead of this branch's base commit; re-merging would introduce duplicates or conflicts.

**Recommended action:** Close PR #1 with a comment referencing the PRs that supersede it, then delete the branch.

---

## Remediation Steps

```bash
# 1. Close PR #1 via GitHub UI (or gh CLI):
gh pr close 1 --comment "Superseded by PRs #8, #9, #15, #16. Closing."

# 2. Delete all stale copilot/* branches (run after closing PR #1):
STALE_BRANCHES=(
  copilot/add-seed-data-departments
  copilot/check-development-milestone-phases
  copilot/check-project-phases-completion
  copilot/complete-all-phases-lis
  copilot/complete-all-phases-of-lis
  copilot/complete-phase-1-task-list
  copilot/complete-phase-2-task-list
  copilot/complete-phase-2-tasks
  copilot/complete-phase-3-tasks
  copilot/complete-phase-3-tasks-again
  copilot/complete-phase-eight-issues
  copilot/complete-rasteronelab-lis-phases
  copilot/create-milestone-from-md-files
  copilot/lis-023-complete-phase-2-tasks
  copilot/lis-029-complete-pending-tasks
  copilot/remove-lombok-and-update-files
  copilot/review-lab-information-system
  copilot/review-phase-1-2-3
  copilot/review-phase-progress
  copilot/setup-complete-project-structure
  copilot/start-development-phase-by-phase
  copilot/update-workflow-billing-first
)

for branch in "${STALE_BRANCHES[@]}"; do
  git push origin --delete "$branch"
done
```

---

## Preventive Measures Implemented (this PR)

| File | Purpose |
|------|---------|
| `.github/pull_request_template.md` | Standardises every PR with a checklist (tests, branch isolation, migrations, OpenAPI) |
| `.github/workflows/ci.yml` | Runs Gradle build + tests + Checkstyle and Angular lint + tests + build on every PR against `main` — **PRs without passing CI cannot be merged** |
| `.github/workflows/stale.yml` | Automatically labels PRs as `stale` after 30 days of inactivity and closes them after 7 more days |

### Recommended: Enable Branch Protection on `main`

In **GitHub → Settings → Branches → Add rule** for `main`:

- [x] Require a pull request before merging
- [x] Require status checks to pass (select `backend` and `frontend` from CI workflow)
- [x] Require branches to be up to date before merging
- [x] Restrict who can push to matching branches

This ensures **no commit can reach `main` without a PR** and passing CI checks going forward.
