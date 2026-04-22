import { startTransition, useDeferredValue, useEffect, useState } from 'react'
import './App.css'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080'

const sourceOptions = ['LINKEDIN', 'GITHUB', 'REFERRAL', 'CAREER_SITE', 'BOOTCAMP']
const employmentOptions = ['FULL_TIME', 'PART_TIME', 'INTERN', 'CONTRACT']
const statusOptions = ['NEW', 'SCREENING', 'INTERVIEW', 'OFFER', 'HIRED', 'REJECTED']

const initialCandidateForm = {
  fullName: '',
  email: '',
  phone: '',
  location: '',
  source: 'GITHUB',
  yearsOfExperience: 1,
  primaryStack: '',
  noticePeriodDays: 14,
  portfolioUrl: '',
}

const initialJobForm = {
  title: '',
  team: '',
  location: '',
  employmentType: 'FULL_TIME',
  level: '',
  minSalary: 25000,
  maxSalary: 40000,
  active: true,
}

const initialApplicationForm = {
  candidateId: '',
  jobPostingId: '',
  status: 'NEW',
  fitScore: 75,
  stageNotes: '',
}

function App() {
  const [dashboard, setDashboard] = useState({ summary: [], pipeline: [], recentApplications: [] })
  const [candidates, setCandidates] = useState([])
  const [jobs, setJobs] = useState([])
  const [applications, setApplications] = useState([])
  const [candidateForm, setCandidateForm] = useState(initialCandidateForm)
  const [jobForm, setJobForm] = useState(initialJobForm)
  const [applicationForm, setApplicationForm] = useState(initialApplicationForm)
  const [searchTerm, setSearchTerm] = useState('')
  const [statusFilter, setStatusFilter] = useState('ALL')
  const [isLoading, setIsLoading] = useState(true)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [message, setMessage] = useState({ type: 'idle', text: '' })
  const deferredSearch = useDeferredValue(searchTerm)

  const query = deferredSearch.trim().toLowerCase()
  const filteredApplications = applications.filter((application) => {
    const matchesStatus = statusFilter === 'ALL' || application.status === statusFilter
    const matchesSearch =
      query.length === 0 ||
      application.candidateName.toLowerCase().includes(query) ||
      application.jobTitle.toLowerCase().includes(query) ||
      application.team.toLowerCase().includes(query) ||
      application.candidateStack.toLowerCase().includes(query)

    return matchesStatus && matchesSearch
  })

  useEffect(() => {
    let cancelled = false

    async function bootstrap() {
      try {
        const [dashboardData, candidateData, jobData, applicationData] = await fetchSnapshot()

        if (cancelled) {
          return
        }

        startTransition(() => {
          setDashboard(dashboardData)
          setCandidates(candidateData)
          setJobs(jobData)
          setApplications(applicationData)
        })
      } catch (error) {
        if (!cancelled) {
          setMessage({ type: 'error', text: error.message })
        }
      } finally {
        if (!cancelled) {
          setIsLoading(false)
        }
      }
    }

    bootstrap()

    return () => {
      cancelled = true
    }
  }, [])

  async function handleCandidateSubmit(event) {
    event.preventDefault()
    setIsSubmitting(true)

    try {
      await apiRequest('/api/candidates', {
        method: 'POST',
        body: JSON.stringify({
          ...candidateForm,
          yearsOfExperience: Number(candidateForm.yearsOfExperience),
          noticePeriodDays: Number(candidateForm.noticePeriodDays),
        }),
      })

      setCandidateForm(initialCandidateForm)
      setMessage({ type: 'success', text: 'Candidate added to the pipeline.' })
      await refreshData({
        setDashboard,
        setCandidates,
        setJobs,
        setApplications,
        setIsLoading,
        setMessage,
      })
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  async function handleJobSubmit(event) {
    event.preventDefault()
    setIsSubmitting(true)

    try {
      await apiRequest('/api/job-postings', {
        method: 'POST',
        body: JSON.stringify({
          ...jobForm,
          minSalary: Number(jobForm.minSalary),
          maxSalary: Number(jobForm.maxSalary),
        }),
      })

      setJobForm(initialJobForm)
      setMessage({ type: 'success', text: 'Role created successfully.' })
      await refreshData({
        setDashboard,
        setCandidates,
        setJobs,
        setApplications,
        setIsLoading,
        setMessage,
      })
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  async function handleApplicationSubmit(event) {
    event.preventDefault()
    setIsSubmitting(true)

    try {
      await apiRequest('/api/applications', {
        method: 'POST',
        body: JSON.stringify({
          ...applicationForm,
          candidateId: Number(applicationForm.candidateId),
          jobPostingId: Number(applicationForm.jobPostingId),
          fitScore: Number(applicationForm.fitScore),
        }),
      })

      setApplicationForm(initialApplicationForm)
      setMessage({ type: 'success', text: 'Application added to live pipeline.' })
      await refreshData({
        setDashboard,
        setCandidates,
        setJobs,
        setApplications,
        setIsLoading,
        setMessage,
      })
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
    } finally {
      setIsSubmitting(false)
    }
  }

  async function handleStatusChange(applicationId, nextStatus) {
    try {
      await apiRequest(`/api/applications/${applicationId}/status`, {
        method: 'PATCH',
        body: JSON.stringify({ status: nextStatus }),
      })

      setMessage({ type: 'success', text: 'Application stage updated.' })
      await refreshData({
        setDashboard,
        setCandidates,
        setJobs,
        setApplications,
        setIsLoading,
        setMessage,
      })
    } catch (error) {
      setMessage({ type: 'error', text: error.message })
    }
  }

  return (
    <div className="shell">
      <header className="hero">
        <div className="hero-copy">
          <p className="eyebrow">Candidate Flow</p>
          <h1>Backend-first hiring dashboard for fast-moving recruitment teams.</h1>
          <p className="lede">
            Spring Boot API, relational data model, validation-driven forms and a responsive
            React control room in one portfolio project.
          </p>
          <div className="hero-actions">
            <span className="badge">Spring Boot</span>
            <span className="badge">React</span>
            <span className="badge">H2 in MSSQL mode</span>
          </div>
        </div>
        <div className="hero-panel">
          <p className="panel-title">What this demo shows</p>
          <ul className="feature-list">
            <li>Candidate onboarding with DTO-based API contracts</li>
            <li>Role management and salary band tracking</li>
            <li>Application pipeline with status transitions</li>
            <li>Dashboard metrics seeded with realistic sample data</li>
          </ul>
        </div>
      </header>

      {message.text ? (
        <div className={`alert alert-${message.type}`}>
          <span>{message.text}</span>
          <button type="button" onClick={() => setMessage({ type: 'idle', text: '' })}>
            Dismiss
          </button>
        </div>
      ) : null}

      <section className="summary-grid">
        {dashboard.summary.map((card) => (
          <article key={card.label} className={`summary-card accent-${card.accent}`}>
            <p>{card.label}</p>
            <strong>{card.value}</strong>
          </article>
        ))}
      </section>

      <section className="main-grid">
        <div className="left-column">
          <section className="panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Overview</p>
                <h2>Pipeline health</h2>
              </div>
              <span className="muted">{isLoading ? 'Refreshing data...' : 'Live snapshot'}</span>
            </div>
            <div className="pipeline-grid">
              {dashboard.pipeline.map((item) => (
                <div key={item.status} className="pipeline-stat">
                  <span>{formatLabel(item.status)}</span>
                  <strong>{item.total}</strong>
                </div>
              ))}
            </div>
          </section>

          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Intake</p>
                <h2>Add candidate</h2>
              </div>
            </div>
            <form className="form-grid" onSubmit={handleCandidateSubmit}>
              <label>
                Full name
                <input
                  value={candidateForm.fullName}
                  onChange={(event) =>
                    setCandidateForm((current) => ({ ...current, fullName: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Email
                <input
                  type="email"
                  value={candidateForm.email}
                  onChange={(event) =>
                    setCandidateForm((current) => ({ ...current, email: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Phone
                <input
                  value={candidateForm.phone}
                  onChange={(event) =>
                    setCandidateForm((current) => ({ ...current, phone: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Location
                <input
                  value={candidateForm.location}
                  onChange={(event) =>
                    setCandidateForm((current) => ({ ...current, location: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Source
                <select
                  value={candidateForm.source}
                  onChange={(event) =>
                    setCandidateForm((current) => ({ ...current, source: event.target.value }))
                  }
                >
                  {sourceOptions.map((option) => (
                    <option key={option} value={option}>
                      {formatLabel(option)}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Experience
                <input
                  type="number"
                  min="0"
                  max="25"
                  value={candidateForm.yearsOfExperience}
                  onChange={(event) =>
                    setCandidateForm((current) => ({
                      ...current,
                      yearsOfExperience: event.target.value,
                    }))
                  }
                  required
                />
              </label>
              <label className="wide">
                Primary stack
                <input
                  value={candidateForm.primaryStack}
                  onChange={(event) =>
                    setCandidateForm((current) => ({ ...current, primaryStack: event.target.value }))
                  }
                  placeholder="Java, Spring Boot, MSSQL"
                  required
                />
              </label>
              <label>
                Notice period
                <input
                  type="number"
                  min="0"
                  max="365"
                  value={candidateForm.noticePeriodDays}
                  onChange={(event) =>
                    setCandidateForm((current) => ({
                      ...current,
                      noticePeriodDays: event.target.value,
                    }))
                  }
                  required
                />
              </label>
              <label className="wide">
                Portfolio URL
                <input
                  value={candidateForm.portfolioUrl}
                  onChange={(event) =>
                    setCandidateForm((current) => ({ ...current, portfolioUrl: event.target.value }))
                  }
                  placeholder="https://github.com/username"
                />
              </label>
              <button className="primary-button" disabled={isSubmitting} type="submit">
                Save candidate
              </button>
            </form>
          </section>

          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Hiring</p>
                <h2>Create role</h2>
              </div>
            </div>
            <form className="form-grid" onSubmit={handleJobSubmit}>
              <label>
                Title
                <input
                  value={jobForm.title}
                  onChange={(event) =>
                    setJobForm((current) => ({ ...current, title: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Team
                <input
                  value={jobForm.team}
                  onChange={(event) =>
                    setJobForm((current) => ({ ...current, team: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Location
                <input
                  value={jobForm.location}
                  onChange={(event) =>
                    setJobForm((current) => ({ ...current, location: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Employment type
                <select
                  value={jobForm.employmentType}
                  onChange={(event) =>
                    setJobForm((current) => ({
                      ...current,
                      employmentType: event.target.value,
                    }))
                  }
                >
                  {employmentOptions.map((option) => (
                    <option key={option} value={option}>
                      {formatLabel(option)}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Level
                <input
                  value={jobForm.level}
                  onChange={(event) =>
                    setJobForm((current) => ({ ...current, level: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Minimum salary
                <input
                  type="number"
                  min="10000"
                  value={jobForm.minSalary}
                  onChange={(event) =>
                    setJobForm((current) => ({ ...current, minSalary: event.target.value }))
                  }
                  required
                />
              </label>
              <label>
                Maximum salary
                <input
                  type="number"
                  min="10000"
                  value={jobForm.maxSalary}
                  onChange={(event) =>
                    setJobForm((current) => ({ ...current, maxSalary: event.target.value }))
                  }
                  required
                />
              </label>
              <label className="checkbox-row">
                <input
                  type="checkbox"
                  checked={jobForm.active}
                  onChange={(event) =>
                    setJobForm((current) => ({ ...current, active: event.target.checked }))
                  }
                />
                Keep role open
              </label>
              <button className="primary-button" disabled={isSubmitting} type="submit">
                Save role
              </button>
            </form>
          </section>

          <section className="panel form-panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Pipeline</p>
                <h2>Create application</h2>
              </div>
            </div>
            <form className="form-grid" onSubmit={handleApplicationSubmit}>
              <label className="wide">
                Candidate
                <select
                  value={applicationForm.candidateId}
                  onChange={(event) =>
                    setApplicationForm((current) => ({
                      ...current,
                      candidateId: event.target.value,
                    }))
                  }
                  required
                >
                  <option value="">Select a candidate</option>
                  {candidates.map((candidate) => (
                    <option key={candidate.id} value={candidate.id}>
                      {candidate.fullName} - {candidate.primaryStack}
                    </option>
                  ))}
                </select>
              </label>
              <label className="wide">
                Role
                <select
                  value={applicationForm.jobPostingId}
                  onChange={(event) =>
                    setApplicationForm((current) => ({
                      ...current,
                      jobPostingId: event.target.value,
                    }))
                  }
                  required
                >
                  <option value="">Select a role</option>
                  {jobs.map((job) => (
                    <option key={job.id} value={job.id}>
                      {job.title} - {job.team}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Stage
                <select
                  value={applicationForm.status}
                  onChange={(event) =>
                    setApplicationForm((current) => ({ ...current, status: event.target.value }))
                  }
                >
                  {statusOptions.map((option) => (
                    <option key={option} value={option}>
                      {formatLabel(option)}
                    </option>
                  ))}
                </select>
              </label>
              <label>
                Fit score
                <input
                  type="number"
                  min="1"
                  max="100"
                  value={applicationForm.fitScore}
                  onChange={(event) =>
                    setApplicationForm((current) => ({ ...current, fitScore: event.target.value }))
                  }
                  required
                />
              </label>
              <label className="wide">
                Notes
                <textarea
                  rows="4"
                  value={applicationForm.stageNotes}
                  onChange={(event) =>
                    setApplicationForm((current) => ({ ...current, stageNotes: event.target.value }))
                  }
                  placeholder="Strong API fundamentals, clean communication, solid testing habits..."
                  required
                />
              </label>
              <button className="primary-button" disabled={isSubmitting} type="submit">
                Save application
              </button>
            </form>
          </section>
        </div>

        <div className="right-column">
          <section className="panel">
            <div className="panel-header">
              <div>
                <p className="section-tag">Control Room</p>
                <h2>Application tracker</h2>
              </div>
              <div className="filters">
                <input
                  value={searchTerm}
                  onChange={(event) => setSearchTerm(event.target.value)}
                  placeholder="Search candidate, team or stack"
                />
                <select value={statusFilter} onChange={(event) => setStatusFilter(event.target.value)}>
                  <option value="ALL">All statuses</option>
                  {statusOptions.map((option) => (
                    <option key={option} value={option}>
                      {formatLabel(option)}
                    </option>
                  ))}
                </select>
              </div>
            </div>

            <div className="application-list">
              {filteredApplications.map((application) => (
                <article key={application.id} className="application-card">
                  <div className="application-top">
                    <div>
                      <p className="application-name">{application.candidateName}</p>
                      <p className="muted">
                        {application.jobTitle} - {application.team}
                      </p>
                    </div>
                    <span className={`status-pill status-${application.status.toLowerCase()}`}>
                      {formatLabel(application.status)}
                    </span>
                  </div>
                  <div className="application-meta">
                    <span>{application.candidateStack}</span>
                    <span>Fit score: {application.fitScore}</span>
                    <span>Applied: {formatDate(application.appliedAt)}</span>
                  </div>
                  <p className="application-notes">{application.stageNotes}</p>
                  <div className="application-footer">
                    <span className="muted">Updated {formatDateTime(application.updatedAt)}</span>
                    <select
                      value={application.status}
                      onChange={(event) => handleStatusChange(application.id, event.target.value)}
                    >
                      {statusOptions.map((option) => (
                        <option key={option} value={option}>
                          {formatLabel(option)}
                        </option>
                      ))}
                    </select>
                  </div>
                </article>
              ))}
            </div>
          </section>

          <section className="double-panel">
            <section className="panel compact-panel">
              <div className="panel-header">
                <div>
                  <p className="section-tag">Talent</p>
                  <h2>Candidate roster</h2>
                </div>
              </div>
              <div className="stack-list">
                {candidates.map((candidate) => (
                  <article key={candidate.id} className="mini-card">
                    <strong>{candidate.fullName}</strong>
                    <p>{candidate.primaryStack}</p>
                    <span>
                      {candidate.yearsOfExperience} yrs - {formatLabel(candidate.source)}
                    </span>
                  </article>
                ))}
              </div>
            </section>

            <section className="panel compact-panel">
              <div className="panel-header">
                <div>
                  <p className="section-tag">Openings</p>
                  <h2>Roles in focus</h2>
                </div>
              </div>
              <div className="stack-list">
                {jobs.map((job) => (
                  <article key={job.id} className="mini-card">
                    <strong>{job.title}</strong>
                    <p>
                      {job.team} - {job.location}
                    </p>
                    <span>
                      {formatLabel(job.employmentType)} - {job.active ? 'Open' : 'Closed'}
                    </span>
                  </article>
                ))}
              </div>
            </section>
          </section>
        </div>
      </section>
    </div>
  )
}

async function apiRequest(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {}),
    },
    ...options,
  })

  if (!response.ok) {
    const errorPayload = await response.json().catch(() => ({}))
    const message =
      errorPayload.message ||
      (errorPayload.validationErrors
        ? Object.values(errorPayload.validationErrors).join(', ')
        : 'Request failed')
    throw new Error(message)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

async function fetchSnapshot() {
  return Promise.all([
    apiRequest('/api/dashboard'),
    apiRequest('/api/candidates'),
    apiRequest('/api/job-postings'),
    apiRequest('/api/applications'),
  ])
}

async function refreshData({
  setDashboard,
  setCandidates,
  setJobs,
  setApplications,
  setIsLoading,
  setMessage,
}) {
  setIsLoading(true)

  try {
    const [dashboardData, candidateData, jobData, applicationData] = await fetchSnapshot()

    startTransition(() => {
      setDashboard(dashboardData)
      setCandidates(candidateData)
      setJobs(jobData)
      setApplications(applicationData)
    })
  } catch (error) {
    setMessage({ type: 'error', text: error.message })
  } finally {
    setIsLoading(false)
  }
}

function formatLabel(value) {
  return value
    .toLowerCase()
    .split('_')
    .map((word) => word[0].toUpperCase() + word.slice(1))
    .join(' ')
}

function formatDate(value) {
  return new Intl.DateTimeFormat('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
  }).format(new Date(value))
}

function formatDateTime(value) {
  return new Intl.DateTimeFormat('en-GB', {
    day: '2-digit',
    month: 'short',
    year: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(value))
}

export default App
