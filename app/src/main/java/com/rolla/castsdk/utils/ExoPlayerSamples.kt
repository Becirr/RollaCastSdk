package com.rolla.castsdk.utils

import com.google.android.gms.cast.MediaInfo
import com.rolla.castsdk.model.Video

class ExoPlayerSamples {

    companion object {

        fun loadVideos(): List<Video> {
            val samples = arrayListOf<Video>()
            samples.add(
                Video(
                    "Clear MP4: Dizzy",
                    "https://html5demos.com/assets/dizzy.mp4",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "video/mp4"
                )
            )
            samples.add(
                Video(
                    "Google Play (MP3)",
                    "https://storage.googleapis.com/exoplayer-test-media-0/play.mp3",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "audio/mp3"
                )
            )
            samples.add(
                Video(
                    "One hour frame counter (MP4)",
                    "https://storage.googleapis.com/exoplayer-test-media-1/mp4/frame-counter-one-hour.mp4",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "video/mp4"
                )
            )
            samples.add(
                Video(
                    "Big Buck Bunny video (FLV)",
                    "https://vod.leasewebcdn.com/bbb.flv?ri=1024&rs=150&start=0",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "video/x-flv"
                )
            )
            samples.add(
                Video(
                    "Google Play (Flac)",
                    "https://storage.googleapis.com/exoplayer-test-media-1/flac/play.flac",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "audio/flac"
                )
            )
            samples.add(
                Video(
                    "Google Play (Ogg, Vorbis)",
                    "https://storage.googleapis.com/exoplayer-test-media-1/ogg/play.ogg",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "audio/ogg"
                )
            )
            samples.add(
                Video(
                    "Screens audio (FMP4)",
                    "https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/audio-141.mp4",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "video/mp4"
                )
            )
            samples.add(
                Video(
                    "Screens 480p (FMP4, H264)",
                    "https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-avc-baseline-480.mp4",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "video/mp4"
                )
            )
            samples.add(
                Video(
                    "Screens 1080p (FMP4, H264)",
                    "https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-137.mp4",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "video/mp4"
                )
            )
            samples.add(
                Video(
                    "Screens 360p (WebM, VP9)",
                    "https://storage.googleapis.com/exoplayer-test-media-1/gen-3/screens/dash-vod-single-segment/video-vp9-360.webm",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "video/webm"
                )
            )
            samples.add(
                Video(
                    "Apple 10s (AAC)",
                    "https://devstreaming-cdn.apple.com/videos/streaming/examples/bipbop_4x3/gear0/fileSequence0.aac",
                    MediaInfo.STREAM_TYPE_BUFFERED,
                    "audio/mp4a-latm"
                )
            )
            return samples
        }

    }
}