/*
 * Created on 27.03.2005
 * Created by Tim Adler
 */
package org.selectbf.gui;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.selectbf.config.DirConfig;
import org.selectbf.config.FtpConfig;
import org.selectbf.event.DownloadEvent;
import org.selectbf.event.DownloadListener;
import org.selectbf.event.FileStatusEvent;
import org.selectbf.event.PersistingStatusEvent;
import org.selectbf.event.PersistingStatusListener;
import org.selectbf.event.ProgressListener;
import org.selectbf.event.FileEndEvent;
import org.selectbf.event.FileEventListener;
import org.selectbf.event.FileStartEvent;
import org.selectbf.event.ProcessingEndEvent;
import org.selectbf.event.ProcessingListener;
import org.selectbf.event.ProgessEvent;
import org.selectbf.event.SourceEvent;
import org.selectbf.event.SourceEventListener;
import org.selectbf.gui.icons.ImageRepository;

public class SelectBfAdapter implements ProcessingListener, SourceEventListener, FileEventListener, ProgressListener, PersistingStatusListener,
        DownloadListener
{
    private WorkArea workArea;

    public SelectBfAdapter(WorkArea workArea)
    {
        this.workArea = workArea;
    }

    public void startProcessing()
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                workArea.addTableItem("Processing started.", "list.start");
            }
        });
    }

    public void endProcessing(final ProcessingEndEvent pee)
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                if (pee.isNormalEnd())
                {
                    workArea.addTableItem("Processing finished.", "list.end");
                } else
                {
                    Exception e = pee.getException();

                    workArea.addTableItem("Error during processing.", "list.error");
                    workArea.addTableItem(e.toString(), null);

                    StackTraceElement[] stackTrace = e.getStackTrace();
                    for (int i = 0; i < stackTrace.length; i++)
                    {
                        workArea.addTableItem(stackTrace[i].toString(), null);
                    }
                }
                workArea.enableStartButton();
            }
        });
    }

    public void beginSourceProcessing(final SourceEvent se)
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                if (se.getConfig() instanceof FtpConfig)
                {
                    FtpConfig ftpc = (FtpConfig) se.getConfig();

                    StringBuffer sb = new StringBuffer();
                    sb.append("[FTP] ");
                    sb.append(ftpc.getHost());
                    sb.append(":");
                    sb.append(ftpc.getPort());
                    sb.append(" ");
                    sb.append(ftpc.getDirectory());

                    workArea.addTableItem(sb.toString(), "list.server");
                } else if (se.getConfig() instanceof DirConfig)
                {
                    workArea.addTableItem(se.getConfig().getDirectory(), "list.folder");
                }
            }
        });

    }

    public void endSourceProcessing(SourceEvent se)
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                workArea.clearNumber();
            }
        });

    }

    private void asynExec(Runnable run)
    {
        workArea.getDisplay().asyncExec(run);
    }

    public void startFileProcessing(final FileStartEvent event)
    {
        asynExec(new Runnable()
        {
            public void run()
            {
                String fileName = event.getFile().getName();
                int number = event.getFileNumber();
                int total = event.getTotalFiles();

                workArea.setNumber(number, total);
                workArea.addTableItem(fileName, "list.sbf.file");
            }
        });

    }

    public void endFileProcessing(final FileEndEvent event)
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                workArea.setProcessingStatus("");
                if (!event.isNormalEnd())
                {
                    String fileName = event.getFile().getName();
                    int number = event.getFileNumber();
                    int total = event.getTotalFiles();
                    workArea.addTableItem("ERROR:" + event.getException(), "list.sbf.file.error");
                }
            }
        });
    }

    public void progressChanged(final ProgessEvent event)
    {
        asynExec(new Runnable()
        {
            public void run()
            {
                float number = (float) event.getNumber();
                float total = (float) event.getTotal();

                float percentage = (number / total) * 100;
                workArea.adjustProgressBar((int) percentage);
            }
        });

    }

    public void fileProcessingStatusChanged(final FileStatusEvent event)
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                switch (event.getStatus())
                {
                case FileStatusEvent.STATUS_DECOMPRESSING:
                    File file = event.getFile();
                    workArea.addTableItem("[Decompress] "+file.getName(), "list.zxml.file");
                    workArea.setProcessingStatus("Decompressing ZXML");
                    break;
                case FileStatusEvent.STATUS_PARSING_XML:
                    workArea.setProcessingStatus("Parsing XML");
                    break;
                case FileStatusEvent.STATUS_PROCESSING_DATA:
                    workArea.setProcessingStatus("Processing data");
                    break;
                case FileStatusEvent.STATUS_PERSISTING:
                    workArea.setProcessingStatus("Persisting to database");
                    break;
                case FileStatusEvent.STATUS_CONSISTENCY_CHECKING:
                    workArea.setProcessingStatus("Checking and correcting XML");
                    break;
                }
            }

        });

    }

    public void persistingStatusChanged(final PersistingStatusEvent event)
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                StringBuffer sb = new StringBuffer();
                sb.append("Persisting Round ");
                sb.append(event.getRound());
                sb.append(" - ");

                switch (event.getStatus())
                {
                case PersistingStatusEvent.STATUS_CHAT:
                    sb.append("Chat");
                    break;
                case PersistingStatusEvent.STATUS_HIGHWAY:
                    sb.append("Vehicle driving");
                    break;
                case PersistingStatusEvent.STATUS_HOSPITAL:
                    sb.append("Heals");
                    break;
                case PersistingStatusEvent.STATUS_KITS:
                    sb.append("Kits");
                    break;
                case PersistingStatusEvent.STATUS_PITSTOP:
                    sb.append("Repairs");
                    break;
                case PersistingStatusEvent.STATUS_PLAYERSTATS:
                    sb.append("PlayerStats");
                    break;
                case PersistingStatusEvent.STATUS_ROUNDINFO:
                    sb.append("Info");
                    break;
                case PersistingStatusEvent.STATUS_SCORES:
                    sb.append("Scores");
                    break;
                }
                workArea.setProcessingStatus(sb.toString());
            }

        });

    }

    public void beginDownload(final DownloadEvent event)
    {
        asynExec(new Runnable()
        {

            public void run()
            {
                workArea.setNumber(event.getNumber(),event.getTotal());
                workArea.addTableItem("[FTP] "+event.getFilename()+" ("+generateFileSizeString(event.getSize())+")", "list.ftp.file");
            }
        });

    }

    public void endDownload(DownloadEvent event)
    {
        // TODO Auto-generated method stub

    }
    
    private String generateFileSizeString(long bytes)
    {
        double kbSize = ((double) bytes)/1024;
        
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        
        return nf.format(kbSize)+"KB";
    }
}
